package it.unina.ingsw.dietiestates_backend.agency.service;

import it.unina.ingsw.dietiestates_backend.agency.model.CreateAgentRequest;
import it.unina.ingsw.dietiestates_backend.agency.model.Agency;
import it.unina.ingsw.dietiestates_backend.agency.model.AgentResponse;
import it.unina.ingsw.dietiestates_backend.agency.repository.AgencyRepository;
import it.unina.ingsw.dietiestates_backend.agency.service.mapper.AgentMapper;
import it.unina.ingsw.dietiestates_backend.auth.model.Role;
import it.unina.ingsw.dietiestates_backend.auth.model.User;
import it.unina.ingsw.dietiestates_backend.auth.repository.RoleRepository;
import it.unina.ingsw.dietiestates_backend.auth.repository.UserRepository;
import it.unina.ingsw.dietiestates_backend.email.EmailService;
import it.unina.ingsw.dietiestates_backend.errors.exception.AgentWithAdminRoleException;
import it.unina.ingsw.dietiestates_backend.errors.exception.CannotRevokeAdminException;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final UserRepository userRepository;
    private final AgencyRepository agencyRepository;
    private final AgentCreationService agentCreationService;
    private final RoleRepository roleRepository;
    private final EmailService emailService;


    public void createAgent(@Valid CreateAgentRequest request) throws MessagingException {
        Agency agency = findAdminAgency();
        agentCreationService.createAgent(request, agency);
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
        var adminAssistantRole = getRoleByName("ROLE_ADMIN_ASSISTANT");

        if(agent.getRoles().contains(adminAssistantRole)){
            throw new AgentWithAdminRoleException("L'agente possiede già il ruolo di assistente amministratore");
        }
        if (agent.getRoles() == null) {
            agent.setRoles(new ArrayList<>());
        }
        agent.getRoles().add(adminAssistantRole);
        userRepository.save(agent);
        emailService.sendAgentPromotedEmail(agent);
    }

    public void deleteAgent(Integer agentId) {
        User agent = userRepository.findById(agentId).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        var adminRole = getRoleByName("ROLE_ADMIN");
        if(agent.getRoles().contains(adminRole)){
            throw new CannotRevokeAdminException("You cannot revoke admin privileges to another administrator!");
        }
        userRepository.revokeAgent(agentId);
    }

    private Role getRoleByName(String roleName) {
        return roleRepository.findByName(roleName)
                .orElseThrow(() -> new IllegalStateException(roleName + " was not initialized"));
    }
}