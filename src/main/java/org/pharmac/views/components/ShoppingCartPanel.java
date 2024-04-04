package org.pharmac.views.components;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

public class ShoppingCartPanel extends Panel {
	public ShoppingCartPanel(String id) {
		super(id);
	}

	public ShoppingCartPanel(String id, IModel<?> model) {
		super(id, model);
	}
}
