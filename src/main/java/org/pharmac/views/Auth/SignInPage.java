package org.pharmac.views.Auth;

import com.giffing.wicket.spring.boot.context.scan.WicketHomePage;
import com.giffing.wicket.spring.boot.context.scan.WicketSignInPage;
import com.giffing.wicket.spring.boot.starter.configuration.extensions.external.spring.security.SecureWebSession;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.pharmac.config.WicketHttpsSession;
import org.pharmac.models.Utilisateur;
import org.pharmac.services.UtilisateurService;
import org.pharmac.views.Home.HomePage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wicketstuff.annotation.mount.MountPath;

import java.io.Serializable;



@WicketSignInPage
@MountPath("signin")
public class SignInPage extends WebPage {
	private String password;
	private String username;

	private static final Logger logger = LoggerFactory.getLogger(SignInPage.class);
	@SpringBean
	private UtilisateurService utilisateurService;

	public SignInPage() {
		add(new FeedbackPanel("feedbackPanel"));

		Form loginForm = new Form<>("loginForm", new CompoundPropertyModel<>(this));
		TextField<String> usernameField = new TextField<>("username");
		PasswordTextField passwordField = new PasswordTextField("password");
		Button submitButton = new Button("submitButton") {
			@Override
			public void onSubmit() {
				// Récupérez les valeurs des champs de saisie
				username = usernameField.getModelObject();
				password = passwordField.getModelObject();

				// Validez les informations d'authentification ici
				if (isValidCredentials(username, password)) {
					// Authentification réussie, redirigez l'utilisateur vers la page d'accueil
					setResponsePage(HomePage.class);
				} else {
					// Affichez un message d'erreur si les informations sont incorrectes
					error("Nom d'utilisateur ou mot de passe incorrect");
				}
			}
		};

		loginForm.add(usernameField, passwordField, submitButton);
		add(loginForm);
	}

	private boolean isValidCredentials(String username, String password) {
		WicketHttpsSession session = (WicketHttpsSession) SecureWebSession.get();
		if (session.signIn(username, password)) {
			Utilisateur utilisateur = utilisateurService.getUtilisateurByUsername(username).get();

			if (!utilisateur.isActif()) {
				return false;
			}
			logger.info("L'utilisateur {} s'est connecté", username);


			session.setAttribute(WicketHttpsSession.SESSION_USER_ID, utilisateur.getId());
			session.setAttribute(WicketHttpsSession.SESSION_USER_LOGIN, utilisateur.getUsername());
			session.setAttribute(WicketHttpsSession.SESSION_USER_EMAIL, utilisateur.getEmailU());
			session.setAttribute(WicketHttpsSession.SESSION_USER_FULL_NAME, utilisateur.getNomU() + ' ' + utilisateur.getPrenomU());
			session.setAttribute(WicketHttpsSession.SESSION_LIST_ROLES, (Serializable) utilisateur.getRoles());
			success("Connexion réussie.");

			// Vérifiez les informations dans votre système d'authentification (base de données, etc.)
			// Retournez true si les informations sont valides, sinon false
			// (Ceci est un exemple, vous devrez implémenter votre propre logique)
			return true;
		}
		return false;
	}
}
