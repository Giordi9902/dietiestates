package it.unina.ingsw.dietiestates_backend.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
@Data
@AllArgsConstructor
public class ChangePasswordRequest {
	@NotBlank
	private String oldPassword;
	@NotBlank
	private String newPassword;
}