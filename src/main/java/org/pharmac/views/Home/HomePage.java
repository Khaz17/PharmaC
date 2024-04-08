package org.pharmac.views.Home;

import com.giffing.wicket.spring.boot.context.scan.WicketHomePage;
import org.pharmac.views.components.BasePage;
import org.pharmac.views.components.TitlePanel;
import org.wicketstuff.annotation.mount.MountPath;

@WicketHomePage @MountPath("accueil")
public class HomePage extends BasePage {
	public HomePage() {
	}
}
