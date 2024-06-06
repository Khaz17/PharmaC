package org.pharmac.views.Utilisateurs;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.pharmac.models.Utilisateur;
import org.pharmac.services.RoleService;
import org.pharmac.services.UtilisateurService;
import org.pharmac.views.Auth.NewPasswordPage;
import org.pharmac.views.components.BasePage;
import org.wicketstuff.annotation.mount.MountPath;

import java.util.ArrayList;
import java.util.List;

@MountPath("admin/utilisateurs")
public class UtilisateursPage extends BasePage {

	@SpringBean
	private UtilisateurService utilisateurService;

	@SpringBean
	private RoleService roleService;

	private List<Utilisateur> utilisateurs = new ArrayList<>();

	public UtilisateursPage() {

		add(new Link<Void>("newutilisateur-page") {
			@Override
			public void onClick() {
				setResponsePage(AddUtilisateurPage.class);
			}
		});

		add(new Label("nbre-utilisateurs", utilisateurService.getAllUtilisateurs().size()));

		ListView<Utilisateur> utilisateurListView = new ListView<Utilisateur>("utilisateursList", utilisateurService.getAllUtilisateurs()) {
			@Override
			protected void populateItem(ListItem<Utilisateur> item) {
				Utilisateur utilisateurRow = item.getModelObject();
//				item.add(new Label("id", utilisateurRow.getId()));
				item.add(new Label("username", utilisateurRow.getUsername()));
				item.add(new Label("nomU", utilisateurRow.getNomU()));
				item.add(new Label("prenomU", utilisateurRow.getPrenomU()));

				item.setOutputMarkupId(true);
//				String statusToDisplay = utilisateurRow.isActif() ? "DÉVERROUILLÉ" : "VERROUILLÉ";
				item.add(new Label("status", Model.of("")){
					@Override
					protected void onConfigure() {
						super.onConfigure();
						setDefaultModelObject(utilisateurRow.isActif() ? "DÉVERROUILLÉ" : "VERROUILLÉ");
					}

					@Override
					protected void onComponentTag(ComponentTag tag) {
						super.onComponentTag(tag);
						if (utilisateurRow.isActif()) {
							tag.append("class", "badge rounded-pill bg-primary", " ");
						} else {
							tag.append("class", "badge rounded-pill bg-danger", " ");
						}
					}
				}.setOutputMarkupId(true));

				item.add(new AjaxLink<Void>("changeStatusButton") {
					@Override
					public void onClick(AjaxRequestTarget target) {
						utilisateurRow.setActif(!utilisateurRow.isActif());
						utilisateurService.updateUtilisateur(utilisateurRow);
						target.add(item);
					}

					@Override
					protected void onComponentTag(ComponentTag tag) {
						super.onComponentTag(tag);
						if (utilisateurRow.isActif()) {
							tag.append("class", "btn btn-sm btn-outline-danger", " ");
							tag.put("value", "Verrouiller");
						} else {
							tag.append("class", "btn btn-sm btn-outline-primary", " ");
							tag.put("value", "Déverrouiller");
						}
					}
				}.setOutputMarkupId(true));
				item.add(new Label("emailU", utilisateurRow.getEmailU()));
				item.add(new Label("telU", utilisateurRow.getTelU()));
				item.add(new Label("roles", utilisateurService.getUtilisateurRolesToString(utilisateurRow)));
				String nomCreateur = utilisateurRow.getCreateur() != null ? utilisateurRow.getCreateur().getNomU() + " " + utilisateurRow.getCreateur().getPrenomU() : "N/A";
				item.add(new Label("createur", nomCreateur));
				item.add(new Link<Void>("gerer-roles") {
					@Override
					public void onClick() {
						PageParameters pageParameters = new PageParameters();
						pageParameters.add("userId", utilisateurRow.getId());
						setResponsePage(AffectationDroitsPage.class, pageParameters);
					}
				});
				item.add(new Link<Void>("edit-user") {
					@Override
					public void onClick() {
						PageParameters pageParameters = new PageParameters();
						pageParameters.add("userId", utilisateurRow.getId());
						setResponsePage(EditUtilisateurPage.class, pageParameters);
					}
				});
				item.add(new Link<Void>("change-password") {
					@Override
					public void onClick() {
						PageParameters pageParameters = new PageParameters();
						pageParameters.add("userId", utilisateurRow.getId());
						setResponsePage(NewPasswordPage.class, pageParameters);
					}
				});
			}
		};

		add(utilisateurListView);
	}
}
