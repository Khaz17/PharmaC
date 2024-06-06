package org.pharmac.config;

import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class LoginSuccessListener implements ApplicationListener<InteractiveAuthenticationSuccessEvent> {
	@Override
	public void onApplicationEvent(InteractiveAuthenticationSuccessEvent event) {
		UserDetails userDetails = (UserDetails) event.getAuthentication().getPrincipal();
		for (GrantedAuthority authority : userDetails.getAuthorities()) {
			System.out.println("Role : " + authority.getAuthority());
		}
	}
}
