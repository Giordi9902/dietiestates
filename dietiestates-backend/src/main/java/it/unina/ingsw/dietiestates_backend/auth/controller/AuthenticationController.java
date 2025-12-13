package it.unina.ingsw.dietiestates_backend.auth.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import it.unina.ingsw.dietiestates_backend.auth.dto.AuthenticationRequest;
import it.unina.ingsw.dietiestates_backend.auth.dto.AuthenticationResponse;
import it.unina.ingsw.dietiestates_backend.auth.dto.RegistrationRequest;
import it.unina.ingsw.dietiestates_backend.auth.dto.RefreshTokenRequest;
import it.unina.ingsw.dietiestates_backend.auth.service.AuthenticationService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.http.auth.InvalidCredentialsException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
@Tag(name="Authentication")
public class AuthenticationController {
	private final AuthenticationService authenticationService;

	@PostMapping("/register")
	public ResponseEntity<Void> register(
			@RequestBody @Valid RegistrationRequest request
	) throws MessagingException {
		authenticationService.register(request);
		return ResponseEntity.accepted().build();
	}

	@PostMapping("/authenticate")
	public ResponseEntity<AuthenticationResponse> authenticate(@Valid @RequestBody AuthenticationRequest request) {
		return ResponseEntity.ok(authenticationService.authenticate(request));
	}

	@PostMapping("/refresh")
	public ResponseEntity<AuthenticationResponse> refresh(@RequestBody @Valid RefreshTokenRequest request){
		return ResponseEntity.ok(authenticationService.refreshToken(request));
	}

	@GetMapping("/activate-account")
	public void confirm(@RequestParam String otp) throws MessagingException {
		authenticationService.activateAccount(otp);
	}
}