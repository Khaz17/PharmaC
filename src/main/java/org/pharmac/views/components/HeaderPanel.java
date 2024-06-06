package org.pharmac.views.components;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.pharmac.models.Utilisateur;
import org.pharmac.services.UtilisateurService;
import org.pharmac.views.Auth.SignInPage;
import org.pharmac.views.Settings.SettingsPage;
import org.pharmac.views.Utilisateurs.ProfilePage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public class HeaderPanel extends Panel {

	private Utilisateur utilisateur;

	@SpringBean
	private UtilisateurService utilisateurService;

	private static final Logger logger = LoggerFactory.getLogger(HeaderPanel.class);

	public HeaderPanel(String id) {
		super(id);

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		Optional<Utilisateur> foundUser = utilisateurService.getUtilisateurByUsername(userDetails.getUsername());
		if (foundUser.isPresent()) {
			utilisateur = foundUser.get();

			add(new Label("logged-username", userDetails.getUsername()));
			add(new Label("logged-fullname", utilisateur.getNomU().toUpperCase() + " " + utilisateur.getPrenomU()));
			add(new Link<Void>("logout") {
				@Override
				public void onClick() {
					SecurityContextHolder.clearContext();
					logger.info("L'utilisateur {} s'est déconnecté", userDetails.getUsername());
					setResponsePage(SignInPage.class);
				}
			});

			add(new Link<Void>("profile-page") {
				@Override
				public void onClick() {
					setResponsePage(ProfilePage.class);
				}
			});

			add(new Link<Void>("settings-page") {
				@Override
				public void onClick() {
					setResponsePage(SettingsPage.class);
				}
			});
		}

	}
}
