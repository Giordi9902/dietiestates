package it.unina.ingsw.dietiestates_backend.agency.service;

import it.unina.ingsw.dietiestates_backend.agency.model.Agency;
import it.unina.ingsw.dietiestates_backend.agency.model.dto.CreateAgentRequest;
import it.unina.ingsw.dietiestates_backend.agency.service.mapper.AgentMapper;
import it.unina.ingsw.dietiestates_backend.auth.model.Role;
import it.unina.ingsw.dietiestates_backend.auth.model.User;
import it.unina.ingsw.dietiestates_backend.auth.repository.RoleRepository;
import it.unina.ingsw.dietiestates_backend.auth.repository.UserRepository;
import it.unina.ingsw.dietiestates_backend.auth.service.OtpService;
import it.unina.ingsw.dietiestates_backend.config.Costants;
import it.unina.ingsw.dietiestates_backend.email.EmailService;
import it.unina.ingsw.dietiestates_backend.errors.exception.UserIsAlreadyAgentException;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static it.unina.ingsw.dietiestates_backend.security.SecurityUtils.generateRandomPassword;

@Service
@RequiredArgsConstructor
public class AgentCreationService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final OtpService otpService;
    private final EmailService emailService;

    @Transactional(rollbackFor = Exception.class)
    public void createAgent(CreateAgentRequest request, Agency agency) throws MessagingException {
        Optional<User> existingUser = userRepository.findByEmail(request.getEmail());
        if (existingUser.isPresent()) {
            addAgentRoleToExistingUser(existingUser.get(), agency);
        } else {
            createAgentFromScratch(request, agency);
        }
    }

    private void createAgentFromScratch(CreateAgentRequest request, Agency agency) throws MessagingException {
        Role agentRole = getRoleByName(Costants.ROLE_AGENT);
        Role userRole = getRoleByName(Costants.ROLE_USER);
        User agent = createAgentEntity(request);
        initializeRolesIfNeeded(agent);
        String rawPassword = generateRandomPassword();
        agent.setPassword(passwordEncoder.encode(rawPassword));
        assignRoles(agent, List.of(userRole, agentRole));
        agent.setAgency(agency);
        agent.setEnabled(false);
        User savedAgent = userRepository.save(agent);
        String otpToken = otpService.generateAndSaveActivationToken(savedAgent);
        emailService.sendAgentCreatedEmail(savedAgent, rawPassword, otpToken);
    }

    private void addAgentRoleToExistingUser(User newAgent, Agency agency) throws MessagingException {
        Role agentRole = getRoleByName(Costants.ROLE_AGENT);

        boolean isAlreadyAgent = newAgent.getRoles().stream().anyMatch(role -> role.getName().equals(Costants.ROLE_AGENT));

        if (isAlreadyAgent) {
            if (newAgent.getAgency() != null && !newAgent.getAgency().getId().equals(agency.getId())) {
                throw new UserIsAlreadyAgentException("L'utente è già un agente per un'altra agenzia.");
            }
        } else {
            initializeRolesIfNeeded(newAgent);
            newAgent.getRoles().add(agentRole);
            newAgent.setAgency(agency);
            userRepository.save(newAgent);
            notifyAgentRoleAdded(newAgent);
        }
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

    private void notifyAgentRoleAdded(User newAgent) throws MessagingException {
        emailService.sendAgentRoleAdded(newAgent);
    }
}