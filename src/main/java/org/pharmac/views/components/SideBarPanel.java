package org.pharmac.views.components;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.pharmac.models.PharmacySettings;
import org.pharmac.services.PharmacySettingsService;
import org.pharmac.views.Categories.CategoriesPage;
import org.pharmac.views.Dashboard.DashboardPage;
import org.pharmac.views.Utilisateurs.AffectationDroitsPage;
import org.pharmac.views.Fournisseurs.EditFournisseurPage;
import org.pharmac.views.Fournisseurs.FournisseursPage;
import org.pharmac.views.Home.HomePage;
import org.pharmac.views.Produits.AddOrEditProduitPage;
import org.pharmac.views.Produits.ProduitsPage;
import org.pharmac.views.Stocks.NewStocksPage;
import org.pharmac.views.Utilisateurs.AddUtilisateurPage;
import org.pharmac.views.Utilisateurs.UtilisateursPage;
import org.pharmac.views.Ventes.NewVentePage;
import org.pharmac.views.Ventes.VentesPage;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SideBarPanel extends Panel {

	@SpringBean
	private PharmacySettingsService pharmacySettingsService;

	public SideBarPanel(String id) {
		super(id);
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		PharmacySettings pharmacySettings = pharmacySettingsService.getPharmacySettings().get();
		String pharmacyNameToDisplay = "Pharmacie " + pharmacySettings.getNomPharma();
		add(new Label("pharmacyName", pharmacyNameToDisplay));
//		add(new Label("pharmacyAddress", pharmacySettings.getAdressePharma()));
//		add(new Label("pharmacyTel", pharmacySettings.getTelPharma()));

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
			@Override
			public boolean isVisible() {
				return authentication != null && authentication.getAuthorities().stream().anyMatch(
						a -> a.getAuthority().equals("ADMIN") ||
						a.getAuthority().equals("GESTIONNAIRE_STOCK"));
			}
		});

		add(new Link<Void>("products-page") {
			@Override
			public void onClick() {
				setResponsePage(ProduitsPage.class);
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
			@Override
			public boolean isVisible() {
				return authentication != null && authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN"));
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

			@Override
			public boolean isVisible() {
				return authentication != null && authentication.getAuthorities().stream().anyMatch(
						a -> a.getAuthority().equals("ADMIN") ||
								a.getAuthority().equals("VENDEUR") ||
								a.getAuthority().equals("GESTIONNAIRE_STOCK"));
			}
		});

		add(new Link<Void>("newvente-page") {
			@Override
			public void onClick() {
				setResponsePage(NewVentePage.class);
			}
			@Override
			public boolean isVisible() {
				return authentication != null && authentication.getAuthorities().stream().anyMatch(
						a -> a.getAuthority().equals("ADMIN") ||
								a.getAuthority().equals("VENDEUR"));
			}
		});

		add(new Link<Void>("ventes-page") {
			@Override
			public void onClick() {
				setResponsePage(VentesPage.class);
			}

			@Override
			public boolean isVisible() {
				return authentication != null && authentication.getAuthorities().stream().anyMatch(
						a -> a.getAuthority().equals("ADMIN") ||
								a.getAuthority().equals("VENDEUR"));
			}
		});

		add(new Link<Void>("newutilisateur-page") {
			@Override
			public void onClick() {
				setResponsePage(AddUtilisateurPage.class);
			}

			@Override
			public boolean isVisible() {
				return authentication != null && authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN"));
			}
		});

		add(new Link<Void>("affectationdroits-page") {
			@Override
			public void onClick() {
				setResponsePage(AffectationDroitsPage.class);
			}

			@Override
			public boolean isVisible() {
				return authentication != null && authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN"));
			}
		});

		add(new Link<Void>("utilisateurs-page") {
			@Override
			public void onClick() {
				setResponsePage(UtilisateursPage.class);
			}

			@Override
			public boolean isVisible() {
				return authentication != null && authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN"));
			}
		});

		add(new Link<Void>("dashboard-page") {
			@Override
			public void onClick() {
				setResponsePage(DashboardPage.class);
			}

			@Override
			public boolean isVisible() {
				return authentication != null && authentication.getAuthorities().stream().anyMatch(a ->
						a.getAuthority().equals("GESTIONNAIRE") ||
						a.getAuthority().equals("ADMIN"));
			}
		});
	}
}
