package it.unina.ingsw.dietiestates_backend.security.oauth2.user;

public class OAuth2UserInfoFactory {
	public static OAuth2UserInfo getOAuth2UserInfo(String registrationId, java.util.Map<String, Object> attributes) {
		if (registrationId.equalsIgnoreCase("google")) {
			return new GoogleOAuth2UserInfo(attributes);
		} else if (registrationId.equalsIgnoreCase("github")) {
			return new GithubOAuth2UserInfo(attributes);
		} else {
			throw new IllegalArgumentException("Sorry! Login with " + registrationId + " is not supported yet.");
		}
	}
}