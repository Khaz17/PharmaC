package org.pharmac.views.Checkout;

import org.pharmac.views.components.BasePage;
import org.pharmac.views.components.TitlePanel;
import org.wicketstuff.annotation.mount.MountPath;

@MountPath("paiement")
public class CheckoutPage extends BasePage {
	public CheckoutPage() {
		add(new TitlePanel("title", "Paiement", "Mettez ici les informations nécessaires au paiement"));
	}
}
