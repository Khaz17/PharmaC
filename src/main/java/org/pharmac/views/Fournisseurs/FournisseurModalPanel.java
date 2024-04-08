package org.pharmac.views.Fournisseurs;

import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.pharmac.models.Fournisseur;
import org.pharmac.services.FournisseurService;

public abstract class FournisseurModalPanel extends Panel {

	@SpringBean
	private FournisseurService service;
	public FournisseurModalPanel(String id) {
		super(id);

		ListView<Fournisseur> listView = new ListView<>("fournisseurs-list", service.getFournisseurs()) {
			@Override
			protected void populateItem(ListItem<Fournisseur> item) {
				Fournisseur fournisseurRow = item.getModelObject();
				item.add(new Label("id", fournisseurRow.getIdF()));
				item.add(new Label("nom", fournisseurRow.getNomF()));
				item.add(new Label("telephone", fournisseurRow.getTelF()));
				item.add(new Label("email", fournisseurRow.getEmailF()));
				item.add(new Label("adresse", fournisseurRow.getAdresseF()));
				item.add(new AjaxEventBehavior("click") {
					@Override
					protected void onEvent(AjaxRequestTarget ajaxRequestTarget) {
						onSelected(ajaxRequestTarget, fournisseurRow);
					}
				});
				item.setOutputMarkupPlaceholderTag(true);

			}
		};

		add(listView);
	}


	public abstract void onSelected(AjaxRequestTarget target, Fournisseur frn);
}
