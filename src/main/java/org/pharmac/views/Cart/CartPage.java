package org.pharmac.views.Cart;

import org.pharmac.views.components.BasePage;
import org.pharmac.views.components.TitlePanel;
import org.wicketstuff.annotation.mount.MountPath;

@MountPath("panier")
public class CartPage extends BasePage {
	public CartPage() {
		add(new TitlePanel("breadcrumb", "Panier", "Voici les produits désirés par le client"));
	}
}
