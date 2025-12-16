package it.unina.ingsw.dietiestates_backend.auth.service;

import it.unina.ingsw.dietiestates_backend.auth.model.OtpToken;
import it.unina.ingsw.dietiestates_backend.auth.model.User;
import it.unina.ingsw.dietiestates_backend.auth.repository.OtpTokenRepository;
import it.unina.ingsw.dietiestates_backend.email.EmailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OtpService {

    private final OtpTokenRepository otpTokenRepository;
    private final EmailService emailService;

    public void sendValidationEmail(User user) throws MessagingException {
        var newToken = generateAndSaveActivationToken(user);
        emailService.sendActivationEmail(user, newToken);
    }

    public OtpToken validateActivationToken(String otpToken) throws MessagingException {
        OtpToken savedToken = otpTokenRepository.findOtpTokenByToken(otpToken)
                .orElseThrow(() -> new RuntimeException("Invalid token"));
        if (LocalDateTime.now().isAfter(savedToken.getExpiresAt())) {
            sendValidationEmail(savedToken.getUser());
            throw new RuntimeException("Activation token has expired. A new token has been sent to the same email address");
        }
        return savedToken;
    }

    public void saveValidatedToken(OtpToken token) {
        otpTokenRepository.save(token);
    }

    public String generateAndSaveActivationToken(User user) {
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

    private String generateActivationToken(int length) {
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