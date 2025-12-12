package it.unina.ingsw.dietiestates_backend.auth.service;

import it.unina.ingsw.dietiestates_backend.auth.model.RefreshToken;
import it.unina.ingsw.dietiestates_backend.auth.repository.RefreshTokenRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepository repository;

    public RefreshTokenService(RefreshTokenRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public RefreshToken createAndSave(String username, String token, Instant expiryDate) {
        repository.deleteAllByUsername(username);

        RefreshToken refreshToken = RefreshToken.builder()
                .username(username)
                .token(token)
                .expiryDate(expiryDate)
                .revoked(false)
                .createdAt(Instant.now())
                .build();
        return repository.save(refreshToken);
    }

    public Optional<RefreshToken> findByToken(String token) {
        return repository.findByToken(token);
    }

    @Transactional
    public void revokeToken(String token) {
        repository.findByToken(token).ifPresent(t -> {
            t.setRevoked(true);
            repository.save(t);
        });
    }
}