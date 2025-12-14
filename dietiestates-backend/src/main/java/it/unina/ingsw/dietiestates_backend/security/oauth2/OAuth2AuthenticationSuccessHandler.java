package it.unina.ingsw.dietiestates_backend.security.oauth2;

import it.unina.ingsw.dietiestates_backend.security.JwtService;
import it.unina.ingsw.dietiestates_backend.security.UserPrincipal;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	private final JwtService jwtService;

	@Value("${app.oauth2.redirect-uri}")
	private String redirectUri;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
		UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
		var claims = new java.util.HashMap<String, Object>();
		claims.put("fullName", userPrincipal.getFullName());
		String accessToken = jwtService.generateToken(claims,userPrincipal);
		String refreshToken = jwtService.generateRefreshToken(userPrincipal);
		String targetUrl = UriComponentsBuilder.fromUriString(redirectUri)
				.fragment("accessToken=" + accessToken + "&refreshToken=" + refreshToken)
				.build().toUriString();
		getRedirectStrategy().sendRedirect(request, response, targetUrl);
	}
}