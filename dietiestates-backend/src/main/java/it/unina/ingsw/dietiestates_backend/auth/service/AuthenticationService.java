package it.unina.ingsw.dietiestates_backend.auth.service;

import it.unina.ingsw.dietiestates_backend.auth.dto.AuthenticationRequest;
import it.unina.ingsw.dietiestates_backend.auth.dto.AuthenticationResponse;
import it.unina.ingsw.dietiestates_backend.auth.dto.RegistrationRequest;
import it.unina.ingsw.dietiestates_backend.auth.dto.RefreshTokenRequest;
import it.unina.ingsw.dietiestates_backend.auth.model.OtpToken;
import it.unina.ingsw.dietiestates_backend.auth.model.RefreshToken;
import it.unina.ingsw.dietiestates_backend.auth.model.User;
import it.unina.ingsw.dietiestates_backend.auth.repository.OtpTokenRepository;
import it.unina.ingsw.dietiestates_backend.auth.repository.RoleRepository;
import it.unina.ingsw.dietiestates_backend.auth.repository.UserRepository;
import it.unina.ingsw.dietiestates_backend.email.EmailService;
import it.unina.ingsw.dietiestates_backend.email.EmailTemplateName;
import it.unina.ingsw.dietiestates_backend.security.JwtService;
import it.unina.ingsw.dietiestates_backend.errors.InvalidRefreshTokenException;
import it.unina.ingsw.dietiestates_backend.errors.InvalidActivationCodeException;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.http.auth.InvalidCredentialsException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;
	private final AuthenticationManager authenticationManager;
	private final RoleRepository roleRepository;
	private final EmailService emailService;
	private final OtpTokenRepository otpTokenRepository;
	private final RefreshTokenService refreshTokenService;
	@Value("${application.security.jwt.refresh-expiration}")
	private long refresh_expiration;

	@Transactional(rollbackOn = Exception.class)
	public void register(RegistrationRequest request) throws MessagingException {
		var userRole = roleRepository.findByName("ROLE_USER")
				.orElseThrow(() -> new IllegalStateException("ROLE USER was not initiated"));
		var user = User.builder()
				.firstname(request.getFirstname())
				.lastname(request.getLastname())
				.email(request.getEmail())
				.password(passwordEncoder.encode(request.getPassword()))
				.accountLocked(false)
				.enabled(false)
				.roles(List.of(userRole))
				.build();
		userRepository.save(user);
		sendValidationEmail(user);
	}

	private void sendValidationEmail(User user) throws MessagingException{
		var newToken = generateAndSaveActivationToken(user);
		emailService.sendActivationEmail(user,newToken);
	}

	public AuthenticationResponse authenticate(AuthenticationRequest request) {

		var auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

		var claims = new HashMap<String, Object>();
		var user = ((User) auth.getPrincipal());
		claims.put("fullName", user.getFullName());

		var jwtToken = jwtService.generateToken(claims, (User) auth.getPrincipal());
		var refreshToken = jwtService.generateRefreshToken(user);
		var expiryInstant = Instant.ofEpochMilli(System.currentTimeMillis() + refresh_expiration);
		refreshTokenService.createAndSave(user.getEmail(), refreshToken, expiryInstant);

		return AuthenticationResponse.builder()
				.jwt_token(jwtToken)
				.refresh_token(refreshToken)
				.build();
	}

	public AuthenticationResponse refreshToken(RefreshTokenRequest request) {
		String incoming = request.getRefresh_token();
		RefreshToken stored = refreshTokenService.findByToken(incoming)
				.orElseThrow(() -> new InvalidRefreshTokenException("Invalid refresh token"));
		if (stored.isRevoked() || stored.getExpiryDate().isBefore(java.time.Instant.now())) {
			throw new InvalidRefreshTokenException("Refresh token expired or revoked");
		}

		User user = userRepository.findByEmail(stored.getUsername()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
		var claims = new java.util.HashMap<String, Object>();
		claims.put("fullName", user.getFullName());
		var newAccess = jwtService.generateToken(claims, user);

		refreshTokenService.revokeToken(incoming);
		var newRefresh = jwtService.generateRefreshToken(user);
		var expiryInstant = java.time.Instant.ofEpochMilli(System.currentTimeMillis() + refresh_expiration);
		refreshTokenService.createAndSave(user.getEmail(), newRefresh, expiryInstant);
		return AuthenticationResponse.builder()
				.jwt_token(newAccess)
				.refresh_token(newRefresh)
				.build();
	}


	@Transactional
	public void activateAccount(String otpToken) throws MessagingException{
		OtpToken savedToken = otpTokenRepository.findOtpTokenByToken(otpToken)
				.orElseThrow(() -> new RuntimeException("Invalid token"));
		if (LocalDateTime.now().isAfter(savedToken.getExpiresAt())) {
			sendValidationEmail(savedToken.getUser());
			throw new RuntimeException("Activation token has expired. A new token has been send to the same email address");
		}

		var user = userRepository.findById(savedToken.getUser().getId())
				.orElseThrow(() -> new UsernameNotFoundException("User not found"));
		user.setEnabled(true);
		userRepository.save(user);

		savedToken.setValidatedAt(LocalDateTime.now());
		otpTokenRepository.save(savedToken);
	}

	public String generateAndSaveActivationToken(User user){
		String generatedToken = generateActivationToken(6);
		var token = OtpToken.builder()
				.token(generatedToken)
				.createdAt(LocalDateTime.now())
				.expiresAt(LocalDateTime.now().plusMinutes(15))
				.user(user)
				.build();
		otpTokenRepository.save(token);
		return generatedToken;
	}

	private String generateActivationToken(int length){
		String characters = "0123456789";
		StringBuilder codeBuilder = new StringBuilder();

		SecureRandom secureRandom = new SecureRandom();

		for (int i = 0; i < length; i++) {
			int randomIndex = secureRandom.nextInt(characters.length());
			codeBuilder.append(characters.charAt(randomIndex));
		}

		return codeBuilder.toString();
	}
}