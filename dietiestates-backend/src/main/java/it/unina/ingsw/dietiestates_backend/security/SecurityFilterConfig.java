package it.unina.ingsw.dietiestates_backend.security;

import it.unina.ingsw.dietiestates_backend.security.oauth2.CustomOAuth2UserService;
import it.unina.ingsw.dietiestates_backend.security.oauth2.CustomOidcUserService;
import it.unina.ingsw.dietiestates_backend.security.oauth2.OAuth2AuthenticationSuccessHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity(securedEnabled = true)
@EnableWebSecurity
@RequiredArgsConstructor
@Slf4j
public class SecurityFilterConfig {
	private final AuthFilter authFilter;
	private final AuthenticationProvider authenticationProvider;
	private final CustomOAuth2UserService customOAuth2UserService;
	private final CustomOidcUserService customOidcUserService;
	private final OAuth2AuthenticationSuccessHandler successHandler;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
		http
				.csrf(csrf->csrf.disable())
				.cors(Customizer.withDefaults())
				.authorizeHttpRequests(req->req.requestMatchers(
								"/auth/**",
								"/login/**",
								"/v2/api-docs",
								"/v3/api-docs",
								"/v3/api-docs/**",
								"/swagger-resources",
								"/swagger-resources/**",
								"/configuration/ui",
								"/configuration/security",
								"/swagger-ui/**",
								"/webjars/**",
								"/swagger-ui.html"
								).permitAll()
						.requestMatchers(HttpMethod.GET,"/listings/**").permitAll()
						.requestMatchers(HttpMethod.POST,"/listings/search").permitAll()
						.anyRequest().authenticated())
				.oauth2Login(oauth2->oauth2
						.userInfoEndpoint(userInfo -> userInfo
								.userService(customOAuth2UserService)
								.oidcUserService(customOidcUserService)
						)
						.successHandler(successHandler)
						.failureHandler(
							(request,response,exception)->{
								log.error("OAuth2 error: {}",exception.getMessage());
							}
				))
				.sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authenticationProvider(authenticationProvider)
				.exceptionHandling(e->e.authenticationEntryPoint(new JsonAuthenticationEntryPoint())
						.accessDeniedHandler(new JsonAccessDeniedHandler()))
				.addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}
}