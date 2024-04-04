package org.pharmac.views.components;

import org.apache.wicket.markup.html.WebPage;

public class BasePage extends WebPage {
	public BasePage() {
//		add(new ShoppingCartPanel("shopping-cart"));
//		add(new ContactPanel("contact-sidemenu"));
//		add(new MobileMenuPanel("mobile-sidemenu"));
		add(new HeaderPanel("header"));
		add(new SideBarPanel("sidebar"));
	}
}
