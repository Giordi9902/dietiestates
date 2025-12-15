package it.unina.ingsw.dietiestates_backend.agency.service.security;

import it.unina.ingsw.dietiestates_backend.agency.model.Agency;
import it.unina.ingsw.dietiestates_backend.auth.model.User;
import it.unina.ingsw.dietiestates_backend.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import static it.unina.ingsw.dietiestates_backend.security.SecurityUtils.hasRole;

@Service("adminSecurity")
@RequiredArgsConstructor
public class AdminSecurity {
	private final UserRepository userRepository;

    public boolean isAdminOfAgentAgency(Integer agentId){
        Authentication auth = SecurityContextHolder.getContext()
                .getAuthentication();
        if(auth==null|| !auth.isAuthenticated()) return false;
        String userEmail = auth.getName();
        User admin = userRepository.findByEmail(userEmail).orElseThrow(()->new RuntimeException("User not found"));

        boolean isAdmin = hasRole(admin,"ROLE_ADMIN");
        boolean isAdminAssistant = hasRole(admin,"ROLE_ADMIN_ASSISTANT");

        // require at least one of the admin roles
        if(!(isAdmin || isAdminAssistant)) return false;

        User agent = userRepository.findById(agentId).orElseThrow(()->new RuntimeException("Agent not found"));
        Agency admin_agency = admin.getAgency();

        // protect against null agent.getAgency()
        boolean isAdminOfAgentAgency = admin_agency != null && agent.getAgency() != null
                && admin_agency.getId().equals(agent.getAgency().getId());

        return (isAdmin || isAdminAssistant) && isAdminOfAgentAgency;
    }
}