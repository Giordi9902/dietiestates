package it.unina.ingsw.dietiestates_backend.security.oauth2;

import it.unina.ingsw.dietiestates_backend.exception.OAuth2AuthenticationProcessingException;
import it.unina.ingsw.dietiestates_backend.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomOidcUserService extends OidcUserService {

    private final CustomOAuth2UserService customOAuth2UserService;

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OidcUser oidcUser = super.loadUser(userRequest);
        try {
            UserPrincipal userPrincipal = (UserPrincipal) customOAuth2UserService.processOAuth2User(userRequest, oidcUser);
            userPrincipal.setOidcDetails(oidcUser.getIdToken(), oidcUser.getUserInfo());
            return userPrincipal;
        } catch (Exception ex) {
            throw new OAuth2AuthenticationProcessingException(ex.getMessage(), ex.getCause());
        }
    }
}