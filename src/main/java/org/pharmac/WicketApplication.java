package org.pharmac;

import org.apache.wicket.Page;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.pharmac.views.Home.HomePage;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class WicketApplication extends WebApplication {
	public static void main(String[] args) throws Exception {
		new SpringApplicationBuilder()
				.sources(WicketApplication.class)
				.run(args);
	}

	@Override
	public Class<? extends Page> getHomePage() {
		return HomePage.class;
	}

	@Override
	protected void init() {
		super.init();
		getComponentInstantiationListeners().add(new SpringComponentInjector(this));
		// Les pages sont montées automatiquement grâce aux annotations @MountPath
	}

}
