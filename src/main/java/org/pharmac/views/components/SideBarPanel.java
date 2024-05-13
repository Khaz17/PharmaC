package org.pharmac.views.components;

import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.pharmac.views.Categories.CategoriesPage;
import org.pharmac.views.Fournisseurs.EditFournisseurPage;
import org.pharmac.views.Fournisseurs.FournisseursPage;
import org.pharmac.views.Home.HomePage;
import org.pharmac.views.Produits.AddOrEditProduitPage;
import org.pharmac.views.Produits.ProduitsPage;
import org.pharmac.views.Stocks.NewStocksPage;
import org.pharmac.views.Ventes.NewVentePage;
import org.pharmac.views.Ventes.VentesPage;

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
				setResponsePage(AddOrEditProduitPage.class);
			}
		});

		add(new Link<Void>("products-page") {
			@Override
			public void onClick() {
				setResponsePage(ProduitsPage.class);
			}
		});

//		add(new Link<Void>("details-page") {
//			@Override
//			public void onClick() {
//				setResponsePage(DetailsProduitPage.class);
//			}
//		});

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

		add(new Link<Void>("stocks-page") {
			@Override
			public void onClick() {
				setResponsePage(NewStocksPage.class);
			}
		});

		add(new Link<Void>("newvente-page") {
			@Override
			public void onClick() {
				setResponsePage(NewVentePage.class);
			}
		});

		add(new Link<Void>("ventes-page") {
			@Override
			public void onClick() {
				setResponsePage(VentesPage.class);
			}
		});
	}
}
