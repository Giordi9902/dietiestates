package it.unina.ingsw.dietiestates_backend.auth.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AuthenticationResponse {
	private String jwt_token;
	private String refresh_token;
}