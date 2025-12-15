package it.unina.ingsw.dietiestates_backend.auth.repository;

import it.unina.ingsw.dietiestates_backend.auth.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Integer> {
	Optional<User> findByEmail(String email);

	Page<User> findAllByAgencyId(Integer agencyId, Pageable pageable);

	@Query(value="SELECT revoke_multiple_agency_roles_and_clear_agency(:agentId)")
	void revokeAgent(@Param("agentId") Integer agentId);
}