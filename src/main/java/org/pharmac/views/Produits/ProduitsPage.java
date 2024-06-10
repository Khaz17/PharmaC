package org.pharmac.views.Produits;

import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.pharmac.models.Produit;
import org.pharmac.services.CategorieService;
import org.pharmac.services.ProduitService;
import org.pharmac.views.components.BasePage;
import org.pharmac.views.components.ConfirmDeletePage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		add(new Link<Void>("ajout-produit") {
			@Override
			public void onClick() {
				setResponsePage(AddOrEditProduitPage.class);
			}

			@Override
			public boolean isVisible() {
				return authentication != null && authentication.getAuthorities().stream().anyMatch(
						a -> a.getAuthority().equals("ADMIN") ||
						a.getAuthority().equals("GESTIONNAIRE_STOCK"));
			}
		});

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

				int stockTotal = produitService.getProduitStockTotal(produitRow.getCodeP());
				item.add(new Label("stockTotal", stockTotal));

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

					@Override
					public boolean isVisible() {
						return authentication != null && authentication.getAuthorities().stream().anyMatch(
								a -> a.getAuthority().equals("ADMIN") ||
										a.getAuthority().equals("GESTIONNAIRE_STOCK"));
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

					@Override
					public boolean isVisible() {
						return authentication != null && authentication.getAuthorities().stream().anyMatch(
								a -> a.getAuthority().equals("ADMIN"));
					}
				});

			}
		};
		listView.setOutputMarkupId(true);
		add(listView);
	}

//	@Override
//	public void renderHead(IHeaderResponse response) {
//		super.renderHead(response);
////		response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(ProduitsPage.class, "js/libs/datatables-btns.js")));
//		response.render(JavaScriptHeaderItem.forReference(new PackageResourceReference(ProduitsPage.class, "datatables-btns.js")));
//	}
}
