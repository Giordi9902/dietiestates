package it.unina.ingsw.dietiestates_backend.agency.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import it.unina.ingsw.dietiestates_backend.agency.model.AgencyDetailsResponse;
import it.unina.ingsw.dietiestates_backend.agency.service.AgencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name="Agency")
@PreAuthorize("hasAnyRole('ADMIN','AGENT')")
public class AgencyController {
	private final AgencyService agencyService;

	@GetMapping("/agency/details")
	public ResponseEntity<AgencyDetailsResponse> getMyAgencyDetails(Authentication authentication){
		AgencyDetailsResponse agency = agencyService.getMyAgencyDetails(authentication);
		return ResponseEntity.ok(agency);
	}
}