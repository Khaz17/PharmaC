package org.pharmac.views.Produits;

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
import org.wicketstuff.annotation.mount.MountPath;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@MountPath("product-details")
public class DetailsProduitPage extends BasePage {

	@SpringBean
	private ProduitService produitService;

	@SpringBean
	private FournisseurService fournisseurService;

	@SpringBean
	private StockService stockService;

	public DetailsProduitPage(PageParameters parameters) {
		StringValue idParam = parameters.get("idProduit");

		if (!idParam.isNull()) {
			Long idProduit = idParam.toLong();
			Optional<Produit> produit = produitService.getProduit(idProduit);
			if (produit.isPresent()) {
				Categorie categorie = produit.get().getCategorieP();
//				List<Fournisseur> fournisseurs = fournisseurService.getProduitFournisseurs(produit.get().getCodeP());
				List<Stock> stocks = stockService.getProduitStocks(produit.get().getCodeP());

				add(new Label("nomCommercial", produit.get().getNomCommercial()));
				add(new Label("dci", produit.get().getDci()));
				add(new Label("voieAdministration", produit.get().getVoieAdministration()));
				add(new Label("prixUnitaire", produit.get().getPrixUnitaire()));
				add(new Label("descriptionP", produit.get().getDescriptionP()));
				add(new Label("categorie", categorie.getLibelleCtg()));
//				add(new Label("fournisseurs", fournisseurs.toString()));

				add(new Link<>("ajouter-stock") {
					@Override
					public void onClick() {
						setResponsePage(NewStocksPage.class, parameters);
					}
				});

				add(new Link<>("edit-produit") {
					@Override
					public void onClick() {

						setResponsePage(AddOrEditProduitPage.class, parameters);
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
				});

				ListView<Stock> listView = new ListView<>("stocks-list", stocks) {
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
						});
					}
				};
				add(listView);

			}
		}
	}
}
