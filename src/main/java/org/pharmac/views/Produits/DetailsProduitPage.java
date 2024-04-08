package org.pharmac.views.Produits;

import org.pharmac.views.components.BasePage;
import org.pharmac.views.components.TitlePanel;
import org.wicketstuff.annotation.mount.MountPath;

@MountPath("product-details")
public class DetailsProduitPage extends BasePage {
	public DetailsProduitPage() {
		add(new TitlePanel("title", "Détails", "Toutes les informations enregistrées sur le produit"));
	}
}
