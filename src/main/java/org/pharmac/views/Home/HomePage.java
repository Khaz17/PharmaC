package org.pharmac.views.Home;

import com.giffing.wicket.spring.boot.context.scan.WicketHomePage;
import org.pharmac.views.components.BasePage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.wicketstuff.annotation.mount.MountPath;

@MountPath("accueil")
@WicketHomePage
public class HomePage extends BasePage {

	private static final Logger logger = LoggerFactory.getLogger(HomePage.class);

	public HomePage() {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		logger.info("L'utilisateur {} s'est connecté", username);
	}
}
