package it.unina.ingsw.dietiestates_backend.agency.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import it.unina.ingsw.dietiestates_backend.agency.model.AgentResponse;
import it.unina.ingsw.dietiestates_backend.agency.model.CreateAgentRequest;
import it.unina.ingsw.dietiestates_backend.agency.service.AdminOfAgency;
import it.unina.ingsw.dietiestates_backend.agency.service.AdminService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
@Tag(name="Admin")
@RequestMapping("/agency")
public class AdminController {
	private final AdminService adminService;

	@GetMapping("/agents")
	public ResponseEntity<Page<AgentResponse>> getAgentsByAgency(
			@PageableDefault(size = 10) Pageable pageable
			){
		Page<AgentResponse> res = adminService.getAgentsByAgency(pageable);
		return ResponseEntity.ok(res);
	}

	@PostMapping("/agents")
	public ResponseEntity<String> createAgent(@Valid @RequestBody CreateAgentRequest request) throws MessagingException {
		adminService.createAgent(request);
		return ResponseEntity.status(HttpStatus.CREATED).body("Agent correctly created");
	}

	@PatchMapping("/agents/{agentId}/promote")
	@AdminOfAgency
	public ResponseEntity<String> promoteToAdmin(@PathVariable Integer agentId) throws MessagingException {
		adminService.promote(agentId);
		return ResponseEntity.status(HttpStatus.OK).body("Agent updated correctly");
	}

	@DeleteMapping("/agents/{agentId}/delete")
	@AdminOfAgency
	public ResponseEntity<String> deleteAgent(@PathVariable Integer agentId){
		adminService.deleteAgent(agentId);
		return ResponseEntity.status(HttpStatus.OK).body("Agent deleted correctly");
	}
}