package it.unina.ingsw.dietiestates_backend.agency.service;

import it.unina.ingsw.dietiestates_backend.agency.model.CreateAgentRequest;
import it.unina.ingsw.dietiestates_backend.agency.model.Agency;
import it.unina.ingsw.dietiestates_backend.agency.model.AgentResponse;
import it.unina.ingsw.dietiestates_backend.agency.repository.AgencyRepository;
import it.unina.ingsw.dietiestates_backend.auth.model.User;
import it.unina.ingsw.dietiestates_backend.auth.repository.RoleRepository;
import it.unina.ingsw.dietiestates_backend.auth.repository.UserRepository;
import it.unina.ingsw.dietiestates_backend.email.EmailService;
import it.unina.ingsw.dietiestates_backend.errors.EmailAlreadyPresentException;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

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
		if(userRepository.findByEmail(request.getEmail()).isPresent()){
			throw new EmailAlreadyPresentException("This email has already been taken");
		}
		User agent = AgentMapper.toEntity(request);
		String rawPassword = generateRandomPassword();
		agent.setPassword(passwordEncoder.encode(rawPassword));
		var userRole = roleRepository.findByName("ROLE_USER").orElseThrow(()->new IllegalStateException("ROLE USER " +
				"was not" +
				" initialized"));
		var agentRole = roleRepository.findByName("ROLE_AGENT")
				.orElseThrow(()->new IllegalStateException("ROLE AGENT was not initialized"));
		agent.getRoles().addAll(List.of(userRole,agentRole));
		var agency = findAdminAgency();
		agent.setAgency(agency);
		agent.setEnabled(true);
		User savedAgent = userRepository.save(agent);
		emailService.sendAgentCreatedEmail(savedAgent,rawPassword);
	}

	private Agency findAdminAgency() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Integer agencyId;
		if (principal instanceof User userPrincipal && userPrincipal.getAgency() != null && userPrincipal.getAgency().getId() != null) {
			agencyId = userPrincipal.getAgency().getId();
		} else {
			throw new IllegalStateException("Unable to determine agency from authentication principal");
		}
		var agency = agencyRepository.findById(agencyId)
				.orElseThrow(() -> new IllegalStateException("Agency not found"));
		return agency;
	}

	public Page<AgentResponse> getAgentsByAgency(Pageable pageable) {
		var agency = findAdminAgency();
		Page<User> agents = userRepository.findAllByAgencyId(agency.getId(), pageable);
		return agents.map(AgentMapper::fromEntity);
	}

	public void promote(Integer agentId) throws MessagingException {
		User agent = userRepository.findById(agentId).orElseThrow(()-> new UsernameNotFoundException("User not found"));
		var adminRole = roleRepository.findByName("ROLE_ADMIN")
				.orElseThrow(()->new IllegalStateException("ROLE ADMIN was not initialized"));
		agent.getRoles().add(adminRole);
		userRepository.save(agent);
		emailService.sendAgentPromotedEmail(agent);
	}

	public void deleteAgent(Integer agentId) {
		userRepository.deleteById(agentId);
	}
}