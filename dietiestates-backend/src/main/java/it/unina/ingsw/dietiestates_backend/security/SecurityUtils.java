package it.unina.ingsw.dietiestates_backend.security;

import it.unina.ingsw.dietiestates_backend.auth.model.User;

import java.security.SecureRandom;
import java.util.Base64;

public class SecurityUtils {
	public static boolean hasRole(User user, String roleName) {
		return user.getRoles().stream()
				.anyMatch(r -> r.getName().equals(roleName));
	}

	public static String generateRandomPassword(){
		SecureRandom secureRandom = new SecureRandom();
		byte[] bytes = new byte[12];
		secureRandom.nextBytes(bytes);
		return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
	}
}