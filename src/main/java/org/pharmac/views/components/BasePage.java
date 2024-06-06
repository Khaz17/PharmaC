package org.pharmac.views.components;

import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.WebPage;

public class BasePage extends WebPage {
	public BasePage() {
//		add(new ShoppingCartPanel("shopping-cart"));
//		add(new ContactPanel("contact-sidemenu"));
//		add(new MobileMenuPanel("mobile-sidemenu"));
		add(new HeaderPanel("header"));
		add(new SideBarPanel("sidebar"));
	}

//	@Override
//	public void renderHead(IHeaderResponse response) {
//		super.renderHead(response);
//		response.render(JavaScriptHeaderItem.forUrl("https://code.jquery.com/jquery-3.7.1.js"));
//	}
}
