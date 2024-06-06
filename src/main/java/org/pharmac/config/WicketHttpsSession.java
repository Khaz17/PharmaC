package org.pharmac.config;

import com.giffing.wicket.spring.boot.starter.configuration.extensions.external.spring.security.SecureWebSession;
import org.apache.wicket.request.Request;

import java.io.Serializable;
import java.util.List;

public class WicketHttpsSession extends SecureWebSession {
	public static final String SESSION_USER_ID = "SESSION_USER_ID";
	public static final String SESSION_USER_LOGIN = "SESSION_USER_LOGIN";
	public static final String SESSION_USER_EMAIL = "SESSION_USER_EMAIL";
	public static final String SESSION_USER_FULL_NAME = "SESSION_USER_FULL_NAME";
	public static final String SESSION_USER_PROFIL = "SESSION_USER_PROFIL";
	public static final String SESSION_USER_PROFIL_ID = "SESSION_USER_PROFIL_ID";
	public static final String SESSION_ACCESS_TOKEN = "SESSION_ACCESS_TOKEN";
	public static final String SESSION_REFRESH_TOKEN = "SESSION_REFRESH_TOKEN";
	public static final String SESSION_LIST_ROLES = "SESSION_LIST_ROLES";


	public WicketHttpsSession(Request request) {
		super(request);
	}

	public static WicketHttpsSession get(){
		return (WicketHttpsSession) WicketHttpsSession.get();
	}

	public Long getUserId() {
		return (Long) this.getAttribute(SESSION_USER_ID);
	}

	public void setUserId(Long userId) {
		this.setAttribute(SESSION_USER_ID, userId);
	}

	public String getUserLogin() {
		return (String) this.getAttribute(SESSION_USER_LOGIN);
	}

	public String getUserEmail() {
		return (String) this.getAttribute(SESSION_USER_EMAIL);
	}

	public void setUserLogin(String userLogin) {
		this.setAttribute(SESSION_USER_LOGIN, userLogin);
	}

	public String getUserFullName() {
		return (String) this.getAttribute(SESSION_USER_FULL_NAME);
	}

	public void setUserFullName(String userFullName) {
		this.setAttribute(SESSION_USER_FULL_NAME, userFullName);
	}

	public String getUserProfil() {
		return (String) this.getAttribute(SESSION_USER_PROFIL);
	}

	public void setUserProfil(String userProfil) {
		this.setAttribute(SESSION_USER_PROFIL, userProfil);
	}

	public String getRoleList(){
		return (String) this.getAttribute(SESSION_LIST_ROLES);
	}

	public void setRoleList(List<Role> roleList){
		this.setAttribute(SESSION_LIST_ROLES, (Serializable) roleList);
	}
}
