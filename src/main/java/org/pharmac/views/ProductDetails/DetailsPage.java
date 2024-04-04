package org.pharmac.views.ProductDetails;

import org.pharmac.views.components.BasePage;
import org.pharmac.views.components.TitlePanel;
import org.wicketstuff.annotation.mount.MountPath;

@MountPath("product-details")
public class DetailsPage extends BasePage {
	public DetailsPage() {
		add(new TitlePanel("title", "Détails", "Toutes les informations enregistrées sur le produit"));
	}
}
