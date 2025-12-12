package it.unina.ingsw.dietiestates_backend.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationRequest {

	@Email(message = "L'email non è ben formattata")
	@NotEmpty(message = "L'email è obbligatoria")
	@NotNull(message = "L'email è obbligatoria")
	private String email;

	@NotEmpty(message = "La password è obbligatoria")
	@NotNull(message = "La password è obbligatoria")
	@Size(min = 8, message = "La password deve essere lunga almeno 8 caratteri")
	private String password;
}