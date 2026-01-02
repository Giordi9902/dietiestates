package it.unina.ingsw.dietiestates_backend.listings.service.security;

import it.unina.ingsw.dietiestates_backend.auth.model.User;
import it.unina.ingsw.dietiestates_backend.auth.repository.UserRepository;
import it.unina.ingsw.dietiestates_backend.listings.repository.ListingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static it.unina.ingsw.dietiestates_backend.security.SecurityUtils.hasRole;

@Service("listingSecurity")
@RequiredArgsConstructor
public class ListingSecurityService {
	private final UserRepository userRepository;
	private final ListingRepository listingRepository;

	public boolean hasBeenListedByAgent(Integer listingId){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if(auth == null || !auth.isAuthenticated()){
			return false;
		}
		String userEmail = auth.getName();
		User user = userRepository.findByEmail(userEmail).orElseThrow(()->new UsernameNotFoundException("User not " +
				"found with this email"));
        boolean isAdmin = hasRole(user,"ROLE_ADMIN");
		boolean isAgent = hasRole(user,"ROLE_AGENT");
		if(!(isAdmin || isAgent)) return false;

		Optional<Integer> listedByAgentId = listingRepository.findAgentIdByListingId(listingId);
		if (listedByAgentId.isEmpty()) {
			return false;
		}
		return user.getId().equals(listedByAgentId.get());
	}
}