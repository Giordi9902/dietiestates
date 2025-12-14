package it.unina.ingsw.dietiestates_backend.security.oauth2.user;

import java.util.Map;


public class GithubOAuth2UserInfo extends OAuth2UserInfo {

	public GithubOAuth2UserInfo(Map<String, Object> attributes) {
		super(attributes);
	}

	@Override
	public String getId() {
		return ((Integer) attributes.get("id")).toString();
	}

	@Override
	public String getName() {
		String name = (String) attributes.get("name");
		// If the full name is not available, fall back to the login username
		if (name == null) {
			return (String) attributes.get("login");
		}
		return name;
	}

	@Override
	public String getEmail() {
		return (String) attributes.get("email");
	}

}