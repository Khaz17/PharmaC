package org.pharmac.views.Produits;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;
import org.pharmac.models.Categorie;
import org.pharmac.models.Produit;
import org.pharmac.models.Stock;
import org.pharmac.services.FournisseurService;
import org.pharmac.services.ProduitService;
import org.pharmac.services.StockService;
import org.pharmac.views.Stocks.EditStocksPage;
import org.pharmac.views.Stocks.NewStocksPage;
import org.pharmac.views.components.BasePage;
import org.pharmac.views.components.ConfirmDeletePage;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.wicketstuff.annotation.mount.MountPath;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@MountPath("details-produit")
public class DetailsProduitPage extends BasePage {

	@SpringBean
	private ProduitService produitService;

	@SpringBean
	private FournisseurService fournisseurService;

	@SpringBean
	private StockService stockService;

	public DetailsProduitPage(PageParameters parameters) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		StringValue idParam = parameters.get("idProduit");

		if (!idParam.isNull()) {
			Long idProduit = idParam.toLong();
			Optional<Produit> produit = produitService.getProduit(idProduit);
			if (produit.isPresent()) {
				Categorie categorie = produit.get().getCategorieP();
//				List<Fournisseur> fournisseurs = fournisseurService.getProduitFournisseurs(produit.get().getCodeP());
				List<Stock> stocks = stockService.getProduitStocks(produit.get().getCodeP());


				add(new Label("full-name", produit.get().getNomComplet()));
				add(new Link<Void>("actions-produit") {
					@Override
					public void onClick() {
					}

					@Override
					public boolean isVisible() {
						return authentication != null && authentication.getAuthorities().stream().anyMatch(
								a -> a.getAuthority().equals("ADMIN") ||
								a.getAuthority().equals("GESTIONNAIRE_STOCK"));
					}
				});

//				add(new Label("titre", "Détails du produit " + produit.get().getNomComplet()));
				add(new Label("nomCommercial", produit.get().getNomCommercial()));
				add(new Label("dci", produit.get().getDci()));
				add(new Label("formeGalenique", produit.get().getFormeGalenique()));
				add(new Label("dosage", produit.get().getDosage()));
				add(new Label("voieAdministration", produit.get().getVoieAdministration()));
				add(new Label("nbreComprimes", produit.get().getNbreComprimes()));
				add(new Label("prixUnitaire", produit.get().getPrixUnitaire()));
				add(new Label("descriptionP", produit.get().getDescriptionP()));
				add(new Label("categorie", categorie.getLibelleCtg()));
//				add(new Label("fournisseurs", fournisseurs.toString()));

				int stockTotal = produitService.getProduitStockTotal(produit.get().getCodeP());
				add(new Label("stockTotal", stockTotal));

				add(new Link<>("ajouter-stock") {
					@Override
					public void onClick() {
						setResponsePage(NewStocksPage.class, parameters);
					}
					@Override
					public boolean isVisible() {
						return authentication != null && authentication.getAuthorities().stream().anyMatch(
								a -> a.getAuthority().equals("ADMIN") ||
										a.getAuthority().equals("GESTIONNAIRE_STOCK"));
					}
				});

				add(new Link<>("edit-produit") {
					@Override
					public void onClick() {

						setResponsePage(AddOrEditProduitPage.class, parameters);
					}
					@Override
					public boolean isVisible() {
						return authentication != null && authentication.getAuthorities().stream().anyMatch(
								a -> a.getAuthority().equals("ADMIN") ||
										a.getAuthority().equals("GESTIONNAIRE_STOCK"));
					}
				});

				add(new Link<>("delete-produit") {
					@Override
					public void onClick() {
						PageParameters deleteParameters = new PageParameters();
						deleteParameters.add("elementType", produit.get().getClass().getSimpleName());
						deleteParameters.add("elementId", produit.get().getCodeP());
						setResponsePage(ConfirmDeletePage.class, deleteParameters);
					}
					@Override
					public boolean isVisible() {
						return authentication != null && authentication.getAuthorities().stream().anyMatch(
								a -> a.getAuthority().equals("ADMIN"));
					}
				});

				ListView<Stock> stockListView = new ListView<>("stocks-list", stocks) {
					@Override
					protected void populateItem(ListItem<Stock> item) {
						Stock stock = item.getModelObject();
						SimpleDateFormat formatter = new SimpleDateFormat("MM/yyyy");
						String datePeremption = formatter.format(stock.getDatePeremption());
						item.add(new Label("datePeremption", datePeremption));
						item.add(new Label("quantiteStk", stock.getQuantiteStk()));
						item.add(new Label("dateAjout", stock.getDateAjout()));
						item.add(new Label("fournisseur", stock.getFournisseur().getNomF()));
						item.add(new Link<>("edit-stock", item.getModel()) {
							@Override
							public void onClick() {
								PageParameters pageParameters = new PageParameters();
								pageParameters.add("idParam", stock.getIdStk());
								setResponsePage(EditStocksPage.class, pageParameters);
							}

							@Override
							public boolean isVisible() {
								return authentication != null && authentication.getAuthorities().stream().anyMatch(
										a -> a.getAuthority().equals("ADMIN") ||
												a.getAuthority().equals("GESTIONNAIRE_STOCK"));
							}
						});
					}
				};
				add(stockListView);

				WebMarkupContainer stockAlertContainer = new WebMarkupContainer("stockAlertContainer");
				Label emptyStockLabel = new Label("empty-stock", "Vous ne possédez aucune boîte de ce stock, ce qui le rend donc indisponible à la vente.");
				stockAlertContainer.add(emptyStockLabel);
				stockAlertContainer.setVisible(produitService.getProduitStockTotal(produit.get().getCodeP()) <= 0);
				add(stockAlertContainer);

				LocalDate now = LocalDate.now();
				LocalDate datePeremption = stocks.size() != 0 ? stocks.get(0).getDatePeremption().toInstant().atZone(ZoneId.systemDefault()).toLocalDate() : LocalDate.now();
				Period tempsRestant = Period.between(now, datePeremption);
//				System.out.println(tempsRestant.getDays());
//				System.out.println(tempsRestant.getMonths());
//				System.out.println(tempsRestant.getYears());

				SimpleDateFormat formatter = new SimpleDateFormat("MM/yyyy");
				String alertDateLabel = stocks.size() != 0 ? formatter.format(stocks.get(0).getDatePeremption()) : formatter.format(new Date());
				WebMarkupContainer closeDateAlertContainer = new WebMarkupContainer("closeDateAlertContainer");
				Label closeDateLabel = new Label("close-date", "La date de péremption la plus proche de vos stocks est " + alertDateLabel + ", dans " +ChronoUnit.DAYS.between(now, datePeremption) + " jours.");
				closeDateAlertContainer.setVisible(false);
				closeDateAlertContainer.add(closeDateLabel);
				add(closeDateAlertContainer);

				WebMarkupContainer passedDateAlertContainer = new WebMarkupContainer("passedDateAlertContainer");
				Label passedDateLabel = new Label("passed-date", "La date de péremption la plus proche est dépassée de " + ChronoUnit.DAYS.between(datePeremption, now) + " jours.");
				passedDateAlertContainer.setVisible(false);
				passedDateAlertContainer.add(passedDateLabel);
				add(passedDateAlertContainer);

				if (tempsRestant.getYears() < 1) {
					closeDateAlertContainer.setVisible(true);
				}

				if (tempsRestant.getDays() < 0) {
					closeDateAlertContainer.setVisible(false);
					passedDateAlertContainer.setVisible(true);
				}

				if (stocks.size() == 0) {
					stockListView.setVisible(false);
					closeDateAlertContainer.setVisible(false);
				}


//				if (tempsRestant.getMonths() < 13) {
//					Label closeDate = new Label("close-date", "La date de péremption la plus proche de vos stocks est le " + datePeremption + ", dans " + tempsRestant.getDays() + ".");
//					closeDate.setVisible(!tempsRestant.isZero());
//					add(closeDate);
//				}


			}
		}
	}
}
