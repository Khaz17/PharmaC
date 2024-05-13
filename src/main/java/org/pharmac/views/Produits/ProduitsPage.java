package org.pharmac.views.Produits;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.pharmac.models.Produit;
import org.pharmac.services.CategorieService;
import org.pharmac.services.ProduitService;
import org.pharmac.views.components.BasePage;
import org.pharmac.views.components.ConfirmDeletePage;
import org.wicketstuff.annotation.mount.MountPath;

import java.util.Collections;
import java.util.List;

@MountPath("produits")
public class ProduitsPage extends BasePage {

	@SpringBean
	private ProduitService produitService;

	@SpringBean
	private CategorieService categorieService;
	public ProduitsPage() {
		LoadableDetachableModel loadableDetachableModel = new LoadableDetachableModel() {
			@Override
			protected Object load() {
				List<Produit> produits = Collections.emptyList();
				produits = produitService.getProduits();
				return produits;
			}
		};

		ListView<Produit> listView = new ListView<Produit>("produits-list", loadableDetachableModel){
			@Override
			protected void populateItem(ListItem<Produit> item) {
				Produit produitRow = item.getModelObject();
				item.add(new Label("id", produitRow.getCodeP()));
				item.add(new Label("nomCommercial", produitRow.getNomCommercial()));
				item.add(new Label("dci", produitRow.getDci()));
				item.add(new Label("voieAdministration", produitRow.getVoieAdministration()));
				item.add(new Label("prixUnitaire", produitRow.getPrixUnitaire()));
				item.add(new Label("categorie", produitService.getProduitCategorie(produitRow.getCodeP()).get().getLibelleCtg()));
				item.add(new Label("stockTotal", produitService.getProduitStockTotal(produitRow.getCodeP())));
				item.add(new Link<>("display-produit", item.getModel()) {
					@Override
					public void onClick() {
						PageParameters parameters = new PageParameters();
						parameters.add("idProduit", produitRow.getCodeP());
						setResponsePage(DetailsProduitPage.class, parameters);
					}
				});
				item.add(new Link<>("edit-produit", item.getModel()) {
					@Override
					public void onClick() {
						PageParameters parameters = new PageParameters();
						parameters.add("idProduit", produitRow.getCodeP());
						setResponsePage(AddOrEditProduitPage.class, parameters);
					}
				});

				item.add(new Link<>("delete-produit", item.getModel()) {
					@Override
					public void onClick() {
						PageParameters deleteParameters = new PageParameters();
						deleteParameters.add("elementType", produitRow.getClass().getSimpleName());
						deleteParameters.add("elementId", produitRow.getCodeP());
						setResponsePage(ConfirmDeletePage.class, deleteParameters);
					}
				});

//				item.add(new Link<>("edit-stk", item.getModel()) {
//					@Override
//					public void onClick() {
//
//					}
//				});
			}
		};
		add(listView);
	}
}
