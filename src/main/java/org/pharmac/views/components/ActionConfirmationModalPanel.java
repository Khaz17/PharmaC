package org.pharmac.views.components;

import com.giffing.wicket.spring.boot.starter.configuration.extensions.external.spring.security.SecureWebSession;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.pharmac.config.WicketHttpsSession;
import org.pharmac.models.Utilisateur;
import org.pharmac.services.UtilisateurService;
import org.pharmac.views.Auth.SignInPage;
import org.pharmac.views.Utilisateurs.ProfilePage;
import org.springframework.security.core.context.SecurityContextHolder;

public class ActionConfirmationModalPanel extends Panel {

	@SpringBean
	private UtilisateurService utilisateurService;

	public ActionConfirmationModalPanel(String id, Utilisateur utilisateur) {
		super(id);

		add(new Label("operation", "Vous êtes sur le point de modifier les informations de votre profil utilisateur. Vous allez être déconnecté et renvoyé sur la page de connexion de l'application."));

		add(new Link<Void>("confirm") {
			@Override
			public void onClick() {
				utilisateurService.updateUtilisateur(utilisateur);
				SecurityContextHolder.clearContext();
//				setResponsePage(SignInPage.class);
			}
		});

		add(new Link<Void>("cancel") {
			@Override
			public void onClick() {
				setResponsePage(ProfilePage.class);
			}
		});
	}

}
