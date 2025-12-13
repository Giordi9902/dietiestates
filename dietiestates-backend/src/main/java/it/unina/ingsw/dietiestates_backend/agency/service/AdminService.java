package it.unina.ingsw.dietiestates_backend.agency.service;

import it.unina.ingsw.dietiestates_backend.agency.model.CreateAgentRequest;
import it.unina.ingsw.dietiestates_backend.agency.model.Agency;
import it.unina.ingsw.dietiestates_backend.agency.model.AgentResponse;
import it.unina.ingsw.dietiestates_backend.agency.repository.AgencyRepository;
import it.unina.ingsw.dietiestates_backend.auth.model.Role;
import it.unina.ingsw.dietiestates_backend.auth.model.User;
import it.unina.ingsw.dietiestates_backend.auth.repository.RoleRepository;
import it.unina.ingsw.dietiestates_backend.auth.repository.UserRepository;
import it.unina.ingsw.dietiestates_backend.email.EmailService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static it.unina.ingsw.dietiestates_backend.security.SecurityUtils.generateRandomPassword;

@Service
@RequiredArgsConstructor
public class AdminService {
	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final PasswordEncoder passwordEncoder;
	private final AgencyRepository agencyRepository;
	private final EmailService emailService;

	public void createAgent(@Valid CreateAgentRequest request) throws MessagingException {
		Agency agency = findAdminAgency();
		Role userRole = getRoleByName("ROLE_USER");
		Role agentRole = getRoleByName("ROLE_AGENT");

		Optional<User> existing = userRepository.findByEmail(request.getEmail());
		if (existing.isPresent()) {
			User agent = existing.get();
			addAgentRoleToUser(agent, agentRole, agency);
		}else {
			createAgentFromScratch(request, userRole, agentRole, agency);
		}
	}

	private void createAgentFromScratch(CreateAgentRequest request, Role userRole, Role agentRole, Agency agency) throws MessagingException {
		User agent = createAgentEntity(request);
		initializeRolesIfNeeded(agent);
		String raw = generateRandomPasswordFor(agent);
		agent.setPassword(passwordEncoder.encode(raw));
		assignRoles(agent, List.of(userRole, agentRole));
		User savedAgent = userRepository.save(agent);
		savedAgent.setAgency(agency);
		savedAgent.setEnabled(true);
		userRepository.save(savedAgent);
		notifyAgentCreated(savedAgent, raw);
	}

	private void addAgentRoleToUser(User newAgent, Role agentRole, Agency agency) throws MessagingException {
		initializeRolesIfNeeded(newAgent);
		assignRoles(newAgent, List.of(agentRole));
		newAgent.setAgency(agency);
		userRepository.save(newAgent);
		notifyAgentRoleAdded(newAgent);
	}

	private Role getRoleByName(String roleName) {
		return roleRepository.findByName(roleName)
				.orElseThrow(() -> new IllegalStateException(roleName + " was not initialized"));
	}

	private void initializeRolesIfNeeded(User user) {
		if (user.getRoles() == null) {
			user.setRoles(new ArrayList<>());
		}
	}

	private void assignRoles(User user, List<Role> roles) {
		user.getRoles().addAll(roles);
	}

	private User createAgentEntity(CreateAgentRequest request) {
		return AgentMapper.toEntity(request);
	}

	private String generateRandomPasswordFor(User agent) {
		return generateRandomPassword();
	}

	private void notifyAgentRoleAdded(User newAgent) throws MessagingException {
		emailService.sendAgentRoleAdded(newAgent);
	}

	private void notifyAgentCreated(User savedAgent, String rawPassword) throws MessagingException {
		emailService.sendAgentCreatedEmail(savedAgent, rawPassword);
	}

	private Agency findAdminAgency() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Integer agencyId;
		if (principal instanceof User userPrincipal && userPrincipal.getAgency() != null && userPrincipal.getAgency().getId() != null) {
			agencyId = userPrincipal.getAgency().getId();
		} else {
			throw new IllegalStateException("Unable to determine agency from authentication principal");
		}
		return agencyRepository.findById(agencyId)
				.orElseThrow(() -> new IllegalStateException("Agency not found"));
	}

	public Page<AgentResponse> getAgentsByAgency(Pageable pageable) {
		var agency = findAdminAgency();
		Page<User> agents = userRepository.findAllByAgencyId(agency.getId(), pageable);
		return agents.map(AgentMapper::fromEntity);
	}

	public void promote(Integer agentId) throws MessagingException {
		User agent = userRepository.findById(agentId).orElseThrow(() -> new UsernameNotFoundException("User not found"));
		var adminRole = getRoleByName("ROLE_ADMIN");
		initializeRolesIfNeeded(agent);
		assignRoles(agent, List.of(adminRole));
		userRepository.save(agent);
		emailService.sendAgentPromotedEmail(agent);
	}

	public void deleteAgent(Integer agentId) {
		userRepository.deleteById(agentId);
	}
}