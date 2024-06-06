package org.pharmac.views.Utilisateurs;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.pharmac.models.Utilisateur;
import org.pharmac.services.UtilisateurService;

public abstract class UtilisateursModalPanel extends Panel {

	@SpringBean
	private UtilisateurService utilisateurService;

	public UtilisateursModalPanel(String id) {
		super(id);

		ListView<Utilisateur> utilisateurListView = new ListView<>("utilisateurs-list", utilisateurService.getAllUtilisateurs()) {
			@Override
			protected void populateItem(ListItem<Utilisateur> item) {
				Utilisateur utilisateurRow = item.getModelObject();
				item.add(new Label("id", utilisateurRow.getId()));
				item.add(new Label("username", utilisateurRow.getUsername()));
				item.add(new Label("nomU", utilisateurRow.getNomU()));
				item.add(new Label("prenomU", utilisateurRow.getPrenomU()));
				item.add(new AjaxLink<Void>("select-user") {
					@Override
					public void onClick(AjaxRequestTarget target) {
						onSelected(target, utilisateurRow);
					}
				});
				item.setOutputMarkupPlaceholderTag(true);
			}
		};
		add(utilisateurListView);
	}

	public abstract void onSelected(AjaxRequestTarget target, Utilisateur utilisateur);
}
