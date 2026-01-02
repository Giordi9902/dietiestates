package it.unina.ingsw.dietiestates_backend.auth.service;

import it.unina.ingsw.dietiestates_backend.auth.dto.AuthenticationRequest;
import it.unina.ingsw.dietiestates_backend.auth.dto.AuthenticationResponse;
import it.unina.ingsw.dietiestates_backend.auth.dto.RefreshTokenRequest;
import it.unina.ingsw.dietiestates_backend.auth.dto.RegistrationRequest;
import it.unina.ingsw.dietiestates_backend.auth.model.OtpToken;
import it.unina.ingsw.dietiestates_backend.auth.model.RefreshToken;
import it.unina.ingsw.dietiestates_backend.auth.model.User;
import it.unina.ingsw.dietiestates_backend.auth.repository.RoleRepository;
import it.unina.ingsw.dietiestates_backend.auth.repository.UserRepository;
import it.unina.ingsw.dietiestates_backend.errors.exception.InvalidRefreshTokenException;
import it.unina.ingsw.dietiestates_backend.security.JwtService;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
    private final RefreshTokenService refreshTokenService;
    private final OtpService otpService;

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
        otpService.sendValidationEmail(user);
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        var auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        var claims = new HashMap<String, Object>();
        var user = ((User) auth.getPrincipal());
        claims.put("fullName", user.getFullName());

        var jwtToken = jwtService.generateToken(claims, user);
        var refreshToken = createAndSaveRefreshToken(user);

        return AuthenticationResponse.builder()
                .jwt_token(jwtToken)
                .refresh_token(refreshToken)
                .build();
    }

    public AuthenticationResponse refreshToken(RefreshTokenRequest request) {
        String incomingToken = request.getRefresh_token();
        RefreshToken storedToken = validateRefreshToken(incomingToken);

        User user = userRepository.findByEmail(storedToken.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return createNewTokens(user, incomingToken);
    }

    @Transactional
    public void activateAccount(String otpToken) throws MessagingException {
        OtpToken savedToken = otpService.validateActivationToken(otpToken);

        var user = userRepository.findById(savedToken.getUser().getId())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        user.setEnabled(true);
        userRepository.save(user);

        savedToken.setValidatedAt(LocalDateTime.now());
        otpService.saveValidatedToken(savedToken);
    }

    private RefreshToken validateRefreshToken(String token) {
        RefreshToken stored = refreshTokenService.findByToken(token)
                .orElseThrow(() -> new InvalidRefreshTokenException("Invalid refresh token"));
        if (stored.isRevoked() || stored.getExpiryDate().isBefore(Instant.now())) {
            throw new InvalidRefreshTokenException("Refresh token expired or revoked");
        }
        return stored;
    }

    private AuthenticationResponse createNewTokens(User user, String oldRefreshToken) {
        var claims = new HashMap<String, Object>();
        claims.put("fullName", user.getFullName());
        var newAccessToken = jwtService.generateToken(claims, user);

        refreshTokenService.revokeToken(oldRefreshToken);
        var newRefreshToken = createAndSaveRefreshToken(user);

        return AuthenticationResponse.builder()
                .jwt_token(newAccessToken)
                .refresh_token(newRefreshToken)
                .build();
    }

    private String createAndSaveRefreshToken(User user) {
        var refreshToken = jwtService.generateRefreshToken(user);
        var expiryInstant = Instant.ofEpochMilli(System.currentTimeMillis() + refresh_expiration);
        refreshTokenService.createAndSave(user.getEmail(), refreshToken, expiryInstant);
        return refreshToken;
    }
}