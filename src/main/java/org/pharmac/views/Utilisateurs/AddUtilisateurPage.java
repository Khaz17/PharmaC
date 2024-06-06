package org.pharmac.views.Utilisateurs;

import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.StringValidator;
import org.pharmac.models.Utilisateur;
import org.pharmac.services.UtilisateurService;
import org.pharmac.views.Ventes.ConfirmationPanel;
import org.pharmac.views.components.BasePage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.wicketstuff.annotation.mount.MountPath;

import java.util.Arrays;
import java.util.List;

@MountPath("admin/new-utilisateur")
public class AddUtilisateurPage extends BasePage {

	private Utilisateur utilisateur = new Utilisateur();

	@SpringBean
	private UtilisateurService utilisateurService;

	private String confirmPassword;

	private static final Logger logger = LoggerFactory.getLogger(AddUtilisateurPage.class);

	public AddUtilisateurPage() {

		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		Utilisateur utilisateurConnecte = utilisateurService.getUtilisateurByUsername(username).get();

		add(new FeedbackPanel("feedbackPanel"));

		String loggedUsername = SecurityContextHolder.getContext().getAuthentication().getName();

		Form<Utilisateur> form = new Form<>("form", new CompoundPropertyModel<>(utilisateur)) {
			@Override
			protected void onSubmit() {
				if (!confirmPassword.equals(utilisateur.getPassword())) {
					error("Les deux mots de passe ne correspondent pas");
					return;
				}
				utilisateur.setCreateur(utilisateurConnecte);
				utilisateurService.createUtilisateur(utilisateur);
				logger.info("L'utilisateur {} a été créé par l'utilisateur {}", utilisateur.getUsername(), loggedUsername);
				setModelObject(new Utilisateur());
				confirmPassword = "";
			}
		};
		form.setOutputMarkupId(true);

		form.add(new RequiredTextField<>("username"));
		form.add(new RequiredTextField<>("prenomU"));
		form.add(new RequiredTextField<>("nomU"));
		form.add(new PasswordTextField("password").setRequired(true).add(StringValidator.minimumLength(8)));
		form.add(new PasswordTextField("confirmPassword", new PropertyModel<>(this, "confirmPassword")).setRequired(true).add(StringValidator.minimumLength(8)));
		form.add(new TextField<>("telU"));
		form.add(new EmailTextField("emailU").setRequired(true));
		form.add(new TextField<>("adresseU").setRequired(true));

		List<Boolean> choices = Arrays.asList(true, false);

		IChoiceRenderer<Boolean> renderer = new IChoiceRenderer<>() {
			@Override
			public Object getDisplayValue(Boolean object) {
				return object ? "Actif" : "Désactivé";
			}

			@Override
			public String getIdValue(Boolean object, int index) {
				return object.toString();
			}

			@Override
			public Boolean getObject(String id, IModel<? extends List<? extends Boolean>> choices) {
				return Boolean.valueOf(id);
			}
		};

		DropDownChoice<Boolean> dropDownChoice = new DropDownChoice<>("actif", choices, renderer);
		form.add(dropDownChoice);

		add(form);
	}
}
