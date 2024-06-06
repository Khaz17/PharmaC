package org.pharmac.views.Utilisateurs;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.pharmac.models.Utilisateur;
import org.pharmac.services.UtilisateurService;
import org.pharmac.views.Auth.NewPasswordPage;
import org.pharmac.views.components.BasePage;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.wicketstuff.annotation.mount.MountPath;

import java.util.Optional;

@MountPath("profil")
public class ProfilePage extends BasePage {

	@SpringBean
	private UtilisateurService utilisateurService;

	public ProfilePage() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		Optional<Utilisateur> utilisateurConnecte = utilisateurService.getUtilisateurByUsername(userDetails.getUsername());
		if (utilisateurConnecte.isPresent()) {
			add(new Label("username", utilisateurConnecte.get().getUsername()));
			add(new Label("nomU", utilisateurConnecte.get().getNomU()));
			add(new Label("prenomU", utilisateurConnecte.get().getPrenomU()));
			add(new Label("telU", utilisateurConnecte.get().getTelU()));
			add(new Label("emailU", utilisateurConnecte.get().getEmailU()));
			add(new Label("adresseU", utilisateurConnecte.get().getAdresseU()));

			add(new Link<Void>("changePassword") {
				@Override
				public void onClick() {
					PageParameters pageParameters = new PageParameters();
					pageParameters.add("userId", utilisateurConnecte.get().getId());
					setResponsePage(NewPasswordPage.class, pageParameters);
				}
			});

			add(new EditProfileModal("editProfile", utilisateurConnecte.get()));
		}

	}
}
