package org.pharmac.views.components;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.request.component.IRequestablePage;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;
import org.pharmac.models.Categorie;
import org.pharmac.models.Fournisseur;
import org.pharmac.models.Produit;
import org.pharmac.services.CategorieService;
import org.pharmac.services.FournisseurService;
import org.pharmac.services.ProduitService;
import org.pharmac.views.Categories.CategoriesPage;
import org.pharmac.views.Fournisseurs.FournisseursPage;
import org.pharmac.views.Home.HomePage;
import org.pharmac.views.Produits.ProduitsPage;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;

public class ConfirmDeletePage extends WebPage {

	@SpringBean
	private FournisseurService fournisseurService;

	@SpringBean
	private CategorieService categorieService;

	@SpringBean
	private ProduitService produitService;

	private transient Optional<Fournisseur> fournisseurToDelete;
	private transient Optional<Categorie> categorieToDelete;
	private transient Optional<Produit> produitToDelete;
	private WebPage next;

	public ConfirmDeletePage(PageParameters parameters) {
		if (parameters.isEmpty()) {
			setResponsePage(FournisseursPage.class);
		}

		StringValue elementTypeValue = parameters.get("elementType");
		StringValue elementIdValue = parameters.get("elementId");
		String elementType = elementTypeValue.toString();
		String elementId = elementIdValue.toString();

		fournisseurToDelete = fournisseurService.getFournisseur(Long.parseLong(elementId));
		categorieToDelete = categorieService.getCategorie(Long.parseLong(elementId));
		produitToDelete = produitService.getProduit(Long.parseLong(elementId));

		if (fournisseurToDelete.isPresent() || categorieToDelete.isPresent() || produitToDelete.isPresent()){
			add(new Label("elementType", elementType.toLowerCase()));
			add(new Label("elementId", elementId));
			add(new Link<Void>("confirm") {
				@Override
				public void onClick() {

					try {
						Class<?> service = Class.forName("org.pharmac.services."+elementType+"Service");
						String nomMethode = "remove"+elementType;
						Method methode = service.getDeclaredMethod(nomMethode, Long.class);
//						Object objet = service.getDeclaredConstructor().newInstance();
						Object objet = new Object();
						if (elementType.equals("Fournisseur")) {
							objet = fournisseurService;
							next = new FournisseursPage();
						} else if (elementType.equals("Categorie")) {
							objet = categorieService;
							next = new CategoriesPage();
						} else if (elementType.equals("Produit")) {
							objet = produitService;
							next = new ProduitsPage();
						}
						Object[] parametres = {Long.parseLong(elementId)};
						Object resultat = methode.invoke(objet, parametres);
					} catch (ClassNotFoundException e) {
						System.err.println("Classe non trouvée : " + e.getMessage());
					} catch (NoSuchMethodException e) {
						System.err.println("Méthode non trouvée : " + e.getMessage());
					} catch (IllegalAccessException e) {
						System.err.println("Accès illégal à la méthode : " + e.getMessage());
					} catch (InvocationTargetException e) {
						System.err.println("Erreur lors de l'appel de la méthode : " + e.getMessage());
					}

//					deleteFournisseur(fournisseurToDelete.get().getIdF());
					setResponsePage(next);
				}
			});
			add(new Link<Void>("cancel") {
				@Override
				public void onClick() {
					setResponsePage(FournisseursPage.class);
				}
			});
		}

	}

	private boolean deleteFournisseur(Long fournisseurToDeleteId) {
		Optional<Fournisseur> fournisseurToDelete = fournisseurService.getFournisseur(fournisseurToDeleteId);
		if (fournisseurToDelete.isPresent()) {
			fournisseurService.removeFournisseur(fournisseurToDelete.get().getIdF());
			return true;
		} else {
			return false;
		}

	}

	private boolean deleteCategorie(Long categorieToDeleteId) {
		Optional<Categorie> categorieToDelete = categorieService.getCategorie(categorieToDeleteId);
		if (categorieToDelete.isPresent()) {
			categorieService.removeCategorie(categorieToDelete.get().getNumCtg());
			return true;
		} else {
			return false;
		}
	}

	private boolean deleteProduit(Long produitToDeleteId) {
		Optional<Produit> produitToDelete = produitService.getProduit(produitToDeleteId);
		if (produitToDelete.isPresent()) {
			produitService.removeProduit(produitToDelete.get().getCodeP());
			return true;
		} else {
			return false;
		}
	}
}
