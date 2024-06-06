package org.pharmac.views.Auth;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.StringValidator;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.pharmac.models.Utilisateur;
import org.pharmac.services.UtilisateurService;
import org.pharmac.views.Utilisateurs.ProfilePage;
import org.pharmac.views.Utilisateurs.UtilisateursPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.wicketstuff.annotation.mount.MountPath;

import java.util.Optional;

@MountPath("new-password")
public class NewPasswordPage extends WebPage {

	private Utilisateur utilisateur = new Utilisateur();
	private String password;
	private String confirmPassword;

	@SpringBean
	private UtilisateurService utilisateurService;

	private static final Logger logger = LoggerFactory.getLogger(NewPasswordPage.class);

	public NewPasswordPage(PageParameters parameters) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();

		add(new FeedbackPanel("feedback"));

		if (parameters.get("userId").isNull()) {
			error("Paramètre 'userId' manquant");
			return;
		}

		Long idValue = parameters.get("userId").toLong();
		Optional<Utilisateur> utilisateurToEdit = utilisateurService.getUtilisateurById(idValue);
		if (utilisateurToEdit.isPresent()) {
			utilisateur = utilisateurToEdit.get();

			Form<NewPasswordPage> form = new Form<>("form", new CompoundPropertyModel<>(this)) {
				@Override
				protected void onSubmit() {
					if (!password.equals(confirmPassword)) {
						error("Les mots de passe ne correspondent pas");
						return;
					}

					utilisateurService.changePassword(utilisateur, password);
					logger.info("Le mot de passe de l'utilisateur {} a été changé avec succès", utilisateur.getUsername());
					//contrôler la redirection en se basant sur la transitivité de l'action, retour sur la page de profil si l'username de l'utilisateur en session correspond à celui dont le mdp est modifié
					if (userDetails.getUsername().equals(utilisateur.getUsername())) {
						setResponsePage(ProfilePage.class);
					} else {
						setResponsePage(UtilisateursPage.class); // Redirection après succès
					}
				}
			};

			form.add(new PasswordTextField("password", new PropertyModel<>(this, "password")).setRequired(true)
					.add(StringValidator.minimumLength(8)));
			form.add(new PasswordTextField("confirmPassword", new PropertyModel<>(this, "confirmPassword")).setRequired(true)
					.add(StringValidator.minimumLength(8)));

			add(form);

			add(new Link<Void>("cancel") {
				@Override
				public void onClick() {
					setResponsePage(UtilisateursPage.class);
				}
			});

		} else {
			error("Utilisateur non trouvé");
		}
	}
}
