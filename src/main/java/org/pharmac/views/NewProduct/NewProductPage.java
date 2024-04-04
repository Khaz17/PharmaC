package org.pharmac.views.NewProduct;

import org.pharmac.views.components.BasePage;
import org.pharmac.views.components.TitlePanel;
import org.wicketstuff.annotation.mount.MountPath;

@MountPath("ajouter-produit")
public class NewProductPage extends BasePage {
	public NewProductPage() {
		add(new TitlePanel("title", "Nouveau produit", "Remplissez les champs avec les informations du nouveau produit"));
	}
}
