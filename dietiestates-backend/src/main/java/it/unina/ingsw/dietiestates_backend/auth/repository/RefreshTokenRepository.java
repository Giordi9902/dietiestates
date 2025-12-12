package it.unina.ingsw.dietiestates_backend.auth.repository;

import it.unina.ingsw.dietiestates_backend.auth.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
    void deleteAllByUsername(String username);
}