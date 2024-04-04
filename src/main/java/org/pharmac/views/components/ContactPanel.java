package org.pharmac.views.components;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

public class ContactPanel extends Panel {
	public ContactPanel(String id) {
		super(id);
	}

	public ContactPanel(String id, IModel<?> model) {
		super(id, model);
	}
}
