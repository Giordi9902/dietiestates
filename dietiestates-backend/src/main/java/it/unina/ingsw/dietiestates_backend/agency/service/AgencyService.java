package it.unina.ingsw.dietiestates_backend.agency.service;

import it.unina.ingsw.dietiestates_backend.agency.model.dto.AgencyDetailsResponse;
import it.unina.ingsw.dietiestates_backend.agency.model.Agency;
import it.unina.ingsw.dietiestates_backend.agency.repository.AgencyRepository;
import it.unina.ingsw.dietiestates_backend.auth.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AgencyService {
	private final AgencyRepository agencyRepository;

	public AgencyDetailsResponse getMyAgencyDetails(Authentication authentication) {
		User user = (User) authentication.getPrincipal();
		Agency agency =
				agencyRepository.findById(user.getAgency().getId()).orElseThrow(()->new IllegalStateException("Agency" +
						" not found"));
		return AgencyDetailsResponse.builder()
				.id(agency.getId())
				.name(agency.getName())
				.phoneNumber(agency.getPhoneNumber())
				.email(agency.getEmail())
				.build();
	}
}