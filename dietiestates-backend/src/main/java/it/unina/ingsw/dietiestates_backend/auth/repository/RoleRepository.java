package it.unina.ingsw.dietiestates_backend.auth.repository;
import it.unina.ingsw.dietiestates_backend.auth.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role,Integer> {
	Optional<Role> findByName(String name);
}