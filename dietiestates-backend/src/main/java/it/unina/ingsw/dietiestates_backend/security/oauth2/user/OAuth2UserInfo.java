package it.unina.ingsw.dietiestates_backend.security.oauth2.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@AllArgsConstructor
public abstract class OAuth2UserInfo {
	protected Map<String, Object> attributes;
	public abstract String getId();
	public abstract String getName();
	public abstract String getEmail();
}