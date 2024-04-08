package org.pharmac.views.Produits;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
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
public class AddProduitPage extends BasePage {

	@SpringBean
	private ProduitService produitService;

	@SpringBean
	private FournisseurService fournisseurService;
	@SpringBean
	private CategorieService categorieService;
	@SpringBean
	private StockService stockService;

	private static final Logger logger = LoggerFactory.getLogger(AddProduitPage.class);

	private Produit produit = new Produit();
	boolean produitIsSet = false;
	private Stock stock = new Stock();
	public AddProduitPage(PageParameters parameters) {

		logger.info("Affichage page produit");
		StringValue idValue = parameters.get("produitToEditId");

		// vérifie si un id de produit est passé dans la page pour savoir s'il s'agit d'un ajout ou d'une modification
		if(!idValue.isNull()) {
			Long produitToEditId = idValue.toLong();
			produit = produitService.getProduit(produitToEditId).get();
			produitIsSet = true;
		}


		WebMarkupContainer stockWindow = new WebMarkupContainer("produitContainer");

		stockWindow.setVisible(produitIsSet);
		add(stockWindow);
		Form<Produit> produitForm = new Form<>("produit-form", new CompoundPropertyModel<>(produit)){
			@Override
			protected void onSubmit() {
				produitService.createOrUpdateProduit(produit);
				stock.setProduit(produit);
				produitIsSet = true;
//				stockWindow.setVisible(produitIsSet);
			}
		};
		produitForm.setModelObject(produit);

		produitForm.add(new RequiredTextField<>("nomCommercial"));
		produitForm.add(new TextField<>("dci"));
		produitForm.add(new TextField<>("descriptionP"));

		List<Categorie> categories = categorieService.getCategories();
		produitForm.add(new DropDownChoice<Categorie>("categorieP",
				categories,
				new ChoiceRenderer<>("libelleCtg", "numCtg")).setRequired(true));
		add(produitForm);


		Form<Stock> stockForm = new Form<>("stock-form", new CompoundPropertyModel<>(stock));
//		Form<Stock> stockForm = new Form<>("stock-form", new CompoundPropertyModel<>(stock)){
//			@Override
//			protected void onSubmit() {
////				produitForm.setModelObject(null);
//				produitForm.setEnabled(false);
//				stock.setDateAjout(LocalDateTime.now());
//				logger.info(stock.getDosage());
//				stockService.createOrUpdateStock(stock);
//
//			}
//		};
		stockForm.add(new RequiredTextField<>("dosage"));
		stockForm.add(new RequiredTextField<>("datePeremption"));
		stockForm.add(new NumberTextField<>("quantiteStk").setRequired(true));
		stockForm.add(new NumberTextField<>("prixUnitaire").setRequired(true));
		stockForm.add(new RequiredTextField<>("voieAdministration"));
		stockForm.add(new Button("submit-stock") {
			@Override
			public void onSubmit() {
				produitForm.setEnabled(false);
				stock.setDateAjout(LocalDateTime.now());
				logger.info("Ajout de stock");
				logger.info(stock.getDosage());
				stockService.createOrUpdateStock(stock);
				setResponsePage(AddProduitPage.class);
			}
		});
		List<Fournisseur> fournisseurs = fournisseurService.getFournisseurs();
		stockForm.add(new DropDownChoice<Fournisseur>("fournisseur",
				fournisseurs,
				new ChoiceRenderer<>("nomF", "idF")));
		add(stockForm);
	}
}
