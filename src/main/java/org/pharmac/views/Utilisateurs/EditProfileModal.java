package org.pharmac.views.Utilisateurs;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.EmailTextField;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.pharmac.models.Utilisateur;
import org.pharmac.services.UtilisateurService;
import org.pharmac.views.components.ActionConfirmationModalPanel;

public class EditProfileModal extends Panel {

	@SpringBean
	private UtilisateurService utilisateurService;

	public EditProfileModal(String id, Utilisateur utilisateur) {
		super(id);

		Form<Utilisateur> form = new Form<>("form", new CompoundPropertyModel<>(utilisateur));

		form.add(new TextField<>("username"));
		form.add(new TextField<>("nomU"));
		form.add(new TextField<>("prenomU"));
		form.add(new TextField<>("telU"));
		form.add(new TextField<>("adresseU"));
		form.add(new EmailTextField("emailU"));

		ActionConfirmationModalPanel actionConfirmation = new ActionConfirmationModalPanel("actionConfirmation", utilisateur);
		actionConfirmation.setOutputMarkupId(true);
		add(actionConfirmation);

		form.add(new AjaxButton("submitForm", form) {
			 @Override
			 protected void onSubmit(AjaxRequestTarget target) {
				 super.onSubmit(target);
				 target.add(actionConfirmation);
				 target.add(EditProfileModal.this);
			 }
		});
		add(form);

		setOutputMarkupId(true);
	}
}
