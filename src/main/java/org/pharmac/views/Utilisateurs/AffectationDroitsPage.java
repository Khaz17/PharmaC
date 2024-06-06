package org.pharmac.views.Utilisateurs;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;
import org.pharmac.config.Role;
import org.pharmac.models.Utilisateur;
import org.pharmac.services.RoleService;
import org.pharmac.services.UtilisateurService;
import org.pharmac.views.components.BasePage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wicketstuff.annotation.mount.MountPath;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@MountPath("admin/affecter-droits")
public class AffectationDroitsPage extends BasePage {

	@SpringBean
	private UtilisateurService utilisateurService;

	@SpringBean
	private RoleService roleService;

	private Utilisateur userToAssign;

	private static final Logger logger = LoggerFactory.getLogger(AffectationDroitsPage.class);

	private List<Role> userRoles = new ArrayList<>();

	public AffectationDroitsPage(PageParameters parameters) {
		StringValue idValue = parameters.get("userId");

		WebMarkupContainer container = new WebMarkupContainer("container");
		container.setOutputMarkupId(true);
		add(container);

		Form form = new Form<>("form");
		container.add(form);

		IModel<String> model = new Model<>("");
		TextField<String> nomUtilisateur = new TextField<>("nomUtilisateur", model);
		nomUtilisateur.setEnabled(false);
		nomUtilisateur.setOutputMarkupId(true);
		form.add(nomUtilisateur);

		List<Role> rolesList = roleService.getRoles();

		List<Role> groupRoles = new ArrayList<>();

		if (!idValue.isNull()) {
			userToAssign = utilisateurService.getUtilisateurById(idValue.toLong()).get();
			model.setObject(userToAssign.getUsername() + " - " + userToAssign.getNomU() + " " + userToAssign.getPrenomU());
			groupRoles.addAll(userToAssign.getRoles());
		}


		CheckGroup<Role> group = new CheckGroup<>("group", groupRoles);
		group.setOutputMarkupId(true);
		form.add(group);
		ListView rolesListView = new ListView<Role>("rolesList", rolesList) {
			@Override
			protected void populateItem(ListItem<Role> item) {
				Role roleCheckLabel = item.getModelObject();
				Check<Role> check = new Check<Role>("check", item.getModel());
				item.add(check);
				item.add(new Label("roleLibelle", roleCheckLabel.getRoleLibelle()));
			}
		};
		rolesListView.setReuseItems(true);
		group.add(rolesListView);
//		group.add(new CheckGroupSelector("groupSelector"));

		UtilisateursModalPanel utilisateursModalPanel = new UtilisateursModalPanel("utilisateurspanel") {
			@Override
			public void onSelected(AjaxRequestTarget target, Utilisateur utilisateur) {
				userToAssign = utilisateur;
				model.setObject(utilisateur.getUsername() + " - " + utilisateur.getNomU() + " " + utilisateur.getPrenomU());
				groupRoles.clear();
				groupRoles.addAll(utilisateur.getRoles());
				target.add(container);
				target.add(nomUtilisateur);
				target.add(group);
			}
		};
		add(utilisateursModalPanel);

//		ListView<Role> rolesListView = new ListView<>("rolesList", rolesList) {
//			@Override
//			protected void populateItem(ListItem<Role> item) {
//				Role role = item.getModelObject();
//				CheckBox check = new CheckBox("droitCheckbox", new PropertyModel<>(role, "roleLibelle"));
//				check.setOutputMarkupId(true);
////				check.add(new OnChangeAjaxBehavior() {
////					@Override
////					protected void onUpdate(AjaxRequestTarget target) {
////						boolean isChecked = check.getModelObject();
////						if (isChecked) {
////							userRoles.add(role);
////							System.out.println("Added role : " + role.getRoleName());
////						} else {
////							userRoles.remove(role);
////							System.out.println("Removed role : " + role.getRoleName());
////						}
////					}
////				});
//				item.add(check);
//				item.add(new Label("roleLibelle", role.getRoleLibelle()));
//			}
//		};


//		container.add(rolesListView);

//		container.add(new Link<Void>("confirm") {
//			@Override
//			public void onClick() {
//
//			}
//		});

//		container.add(new SubmitLink("confirm"){
//			@Override
//			public void onSubmit() {
//
//			}
//		});

		form.add(new AjaxButton("confirm") {
			@Override
			protected void onSubmit(AjaxRequestTarget target) {
				List<Role> selectedRoles = (List<Role>) group.getDefaultModelObject();
				System.out.println("L'utilisateur a été mis à jour");
				for (Role r : selectedRoles) {
					System.out.println(r.getRoleName());
				}
				utilisateurService.affecterDroits(userToAssign, selectedRoles);
				logger.info("L'utilisateur {} a désormais les rôles : {}", userToAssign.getUsername(), selectedRoles);
			}
		});
	}
}
