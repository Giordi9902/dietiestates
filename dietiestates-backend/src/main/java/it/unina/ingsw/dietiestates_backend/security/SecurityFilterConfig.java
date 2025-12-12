package it.unina.ingsw.dietiestates_backend.security;

import lombok.RequiredArgsConstructor;
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
public class SecurityFilterConfig {
	private final AuthFilter authFilter;
	private final AuthenticationProvider authenticationProvider;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
		http
				.csrf(csrf->csrf.disable())
				.cors(Customizer.withDefaults())
				.authorizeHttpRequests(req->req.requestMatchers(
								"/auth/**",
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
						.requestMatchers(HttpMethod.GET, "/listings/*", "/listings/search").permitAll()
						.anyRequest().authenticated())
				.sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authenticationProvider(authenticationProvider)
				.addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}
}