package it.unina.ingsw.dietiestates_backend.auth.repository;

import it.unina.ingsw.dietiestates_backend.auth.model.OtpToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OtpTokenRepository extends JpaRepository<OtpToken,Integer> {
	Optional<OtpToken> findOtpTokenByToken(String token);
}