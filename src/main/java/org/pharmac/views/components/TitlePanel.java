package org.pharmac.views.components;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;

public class TitlePanel extends Panel {
	public TitlePanel(String id, String title, String description) {
		super(id);
		add(new Label("title", title));
		add(new Label("description", description));
	}
}
