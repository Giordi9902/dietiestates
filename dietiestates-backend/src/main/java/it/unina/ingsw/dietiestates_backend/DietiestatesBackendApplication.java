package it.unina.ingsw.dietiestates_backend;

import it.unina.ingsw.dietiestates_backend.agency.repository.AgencyRepository;
import it.unina.ingsw.dietiestates_backend.auth.model.AuthProvider;
import it.unina.ingsw.dietiestates_backend.auth.model.Role;
import it.unina.ingsw.dietiestates_backend.auth.model.User;
import it.unina.ingsw.dietiestates_backend.auth.repository.RoleRepository;
import it.unina.ingsw.dietiestates_backend.auth.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.List;

@SpringBootApplication
@EnableJpaAuditing
public class DietiestatesBackendApplication {
	@Bean
	public CommandLineRunner runner(RoleRepository roleRepository,
									UserRepository userRepository,
									AgencyRepository agencyRepository,
									PasswordEncoder passwordEncoder) {
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
			if (roleRepository.findByName("ROLE_ADMIN_ASSISTANT").isEmpty()) {
				roleRepository.save(
						Role.builder().name("ROLE_ADMIN_ASSISTANT").build()
				);
			}

			Role adminRole = roleRepository.findByName("ROLE_ADMIN").orElseThrow();

			var agencies = agencyRepository.findAll();
			for (var agency : agencies) {
				String adminEmail = "admin@" + agency.getName().toLowerCase().replaceAll("\\s+", "") + ".it";
				if (userRepository.findByEmail(adminEmail).isEmpty()) {
					var admin = User.builder()
							.firstname("Admin")
							.lastname(agency.getName())
							.email(adminEmail)
							.password(passwordEncoder.encode("admin1234"))
							.accountLocked(false)
							.enabled(true)
							.provider(AuthProvider.local)
							.roles(List.of(adminRole))
							.agency(agency)
							.build();
					userRepository.save(admin);
				}
			}
		};
	}

	public static void main(String[] args) {
		SpringApplication.run(DietiestatesBackendApplication.class, args);
	}

}