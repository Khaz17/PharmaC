package org.pharmac.views.ProductsList;

import org.pharmac.views.components.BasePage;
import org.pharmac.views.components.TitlePanel;
import org.wicketstuff.annotation.mount.MountPath;

@MountPath("produits")
public class ProductsPage extends BasePage {
	public ProductsPage() {
		add(new TitlePanel("title", "Produits", "Voici la liste des produits que vous avez enregistrés"));
	}
}
