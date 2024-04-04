package org.pharmac.views.components;

import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.pharmac.views.Categories.CategoriesPage;
import org.pharmac.views.EditFournisseurPage.EditFournisseurPage;
import org.pharmac.views.Fournisseurs.FournisseursPage;
import org.pharmac.views.Home.HomePage;
import org.pharmac.views.NewProduct.NewProductPage;
import org.pharmac.views.ProductDetails.DetailsPage;
import org.pharmac.views.ProductsList.ProductsPage;

public class SideBarPanel extends Panel {
	public SideBarPanel(String id) {
		super(id);
		add(new Link<Void>("home-page"){
			@Override
			public void onClick() {
				setResponsePage(HomePage.class);
			}
		});

		add(new Link<Void>("newproduct-page") {
			@Override
			public void onClick() {
				setResponsePage(NewProductPage.class);
			}
		});

		add(new Link<Void>("products-page") {
			@Override
			public void onClick() {
				setResponsePage(ProductsPage.class);
			}
		});

		add(new Link<Void>("details-page") {
			@Override
			public void onClick() {
				setResponsePage(DetailsPage.class);
			}
		});

		add(new Link<Void>("fournisseurs-page") {
			@Override
			public void onClick() {
				setResponsePage(FournisseursPage.class);
			}
		});

		add(new Link<Void>("editfournisseur-page") {
			@Override
			public void onClick() {
				setResponsePage(EditFournisseurPage.class);
			}
		});

		add(new Link<Void>("categories-page") {
			@Override
			public void onClick() {
				setResponsePage(CategoriesPage.class);
			}
		});
	}
}
