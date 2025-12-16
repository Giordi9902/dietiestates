package it.unina.ingsw.dietiestates_backend.agency.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import it.unina.ingsw.dietiestates_backend.agency.model.dto.AgentResponse;
import it.unina.ingsw.dietiestates_backend.agency.model.dto.CreateAgentRequest;
import it.unina.ingsw.dietiestates_backend.agency.service.security.AdminOfAgency;
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
@PreAuthorize("hasAnyRole('ADMIN','ADMIN_ASSISTANT')")
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
	public ResponseEntity<Void> createAgent(@Valid @RequestBody CreateAgentRequest request) throws MessagingException {
		adminService.createAgent(request);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@PatchMapping("/agents/{agentId}/promote")
	@AdminOfAgency
	public ResponseEntity<Void> promoteToAdmin(@PathVariable Integer agentId) throws MessagingException {
		adminService.promote(agentId);
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@DeleteMapping("/agents/{agentId}/delete")
	@AdminOfAgency
	public ResponseEntity<Void> deleteAgent(@PathVariable Integer agentId){
		adminService.deleteAgent(agentId);
		return ResponseEntity.status(HttpStatus.OK).build();
	}
}