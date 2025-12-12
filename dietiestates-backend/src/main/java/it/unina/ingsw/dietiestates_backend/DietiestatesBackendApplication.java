package it.unina.ingsw.dietiestates_backend;

import it.unina.ingsw.dietiestates_backend.auth.model.Role;
import it.unina.ingsw.dietiestates_backend.auth.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class DietiestatesBackendApplication {
	@Bean
	public CommandLineRunner runner(RoleRepository roleRepository) {
		return args -> {
			if (roleRepository.findByName("ROLE_USER").isEmpty()) {
				roleRepository.save(
						Role.builder().name("ROLE_USER").build()
				);
			}
			if (roleRepository.findByName("ROLE_ADMIN").isEmpty()) {
				roleRepository.save(
						Role.builder().name("ROLE_ADMIN").build()
				);
			}
			if (roleRepository.findByName("ROLE_AGENT").isEmpty()) {
				roleRepository.save(
						Role.builder().name("ROLE_AGENT").build()
				);
			}
		};
	}

	public static void main(String[] args) {
		SpringApplication.run(DietiestatesBackendApplication.class, args);
	}

}