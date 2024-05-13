package org.pharmac.views.Produits;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;
import org.pharmac.models.Categorie;
import org.pharmac.models.Fournisseur;
import org.pharmac.models.Produit;
import org.pharmac.models.Stock;
import org.pharmac.services.CategorieService;
import org.pharmac.services.FournisseurService;
import org.pharmac.services.ProduitService;
import org.pharmac.services.StockService;
import org.pharmac.views.components.BasePage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wicketstuff.annotation.mount.MountPath;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@MountPath("ajouter-produit")
public class AddOrEditProduitPage extends BasePage {

	@SpringBean
	private ProduitService produitService;

	@SpringBean
	private FournisseurService fournisseurService;
	@SpringBean
	private CategorieService categorieService;
	@SpringBean
	private StockService stockService;

	private static final Logger logger = LoggerFactory.getLogger(AddOrEditProduitPage.class);

	private Produit produit = new Produit();
	private boolean produitIsSet = false;
	private Stock stock = new Stock();

	public AddOrEditProduitPage(PageParameters parameters) {

		logger.info("Affichage page produit");
		StringValue idValue = parameters.get("idProduit");

		// vérifie si un id de produit est passé dans la page pour savoir s'il s'agit d'un ajout ou d'une modification
		if (!idValue.isNull()) {
			Long produitToEditId = idValue.toLong();
			produit = produitService.getProduit(produitToEditId).get();
			produitIsSet = true;
		}
		stock.setProduit(produit);

		WebMarkupContainer stockWindow = new WebMarkupContainer("produitContainer");


		LoadableDetachableModel stocksLoadableList = new LoadableDetachableModel() {
			@Override
			protected Object load() {
				List<Stock> stocks = Collections.emptyList();
				stocks = produitService.getProduitStocks(produit.getCodeP());
				return stocks;
			}
		};

		ListView<Stock> stockListView = new ListView<Stock>("stocks-list", stocksLoadableList) {
			@Override
			protected void populateItem(ListItem<Stock> item) {
				Stock stockRow = item.getModelObject();
				item.add(new Label("datePeremption", stockRow.getDatePeremption().toString()));
				item.add(new Label("quantiteStk", stockRow.getQuantiteStk()));
				item.add(new Label("dateAjout", stockRow.getDateAjout()));
			}
		};
		stockWindow.add(stockListView);

		stockWindow.setVisible(produitIsSet);

		add(stockWindow);
		Form<Produit> produitForm = new Form<>("produit-form", new CompoundPropertyModel<>(produit));
		produitForm.setModelObject(produit);

		produitForm.add(new RequiredTextField<>("nomCommercial"));
		produitForm.add(new RequiredTextField<>("dci"));
		produitForm.add(new TextField<>("voieAdministration"));
		produitForm.add(new NumberTextField<>("prixUnitaire").setRequired(true));
		produitForm.add(new TextField<>("descriptionP"));

		List<Categorie> categories = categorieService.getCategories();
		produitForm.add(new DropDownChoice<Categorie>("categorieP",
				categories,
				new ChoiceRenderer<>("libelleCtg", "numCtg")).setRequired(true));
		produitForm.add(new Button("submit-produit") {
			@Override
			public void onSubmit() {
				super.onSubmit();
				produitService.createOrUpdateProduit(produit);
				produitIsSet = true;
				stockWindow.setVisible(produitIsSet);
			}
		});
		add(produitForm);


		WebMarkupContainer container = new WebMarkupContainer("container");
		container.setOutputMarkupId(true);
		add(container);

		FeedbackPanel feedbackPanel = new FeedbackPanel("feedback");
		feedbackPanel.setOutputMarkupId(true);

		Form<Stock> stockForm = new Form<>("stock-form", new CompoundPropertyModel<>(stock));
		stockForm.add(new DateTextField("datePeremption", "yyyy-MM-dd"));
		stockForm.add(new NumberTextField<>("quantiteStk").setRequired(true));
		container.add(stockForm);
		container.add(feedbackPanel);
		stockForm.add(new AjaxButton("submit-stock") {
			@Override
			protected void onSubmit(AjaxRequestTarget target) {
				super.onSubmit(target);
				produitForm.setEnabled(false);
				stock.setDateAjout(LocalDateTime.now());
				stockService.createOrUpdateStock(stock);
				target.add(container);
				stock = new Stock();
			}

			@Override
			protected void onError(AjaxRequestTarget target) {
				super.onError(target);
				target.add(feedbackPanel);
			}
		});
		List<Fournisseur> fournisseurs = fournisseurService.getFournisseurs();
		stockForm.add(new DropDownChoice<Fournisseur>("fournisseur",
				fournisseurs,
				new ChoiceRenderer<>("nomF", "idF")));

	}
}
