package it.unina.ingsw.dietiestates_backend.agency.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateAgentRequest {
	@NotEmpty(message="Firstname is mandatory")
	@NotNull(message="Firstname is mandatory")
	private String firstname;
	@NotEmpty(message = "Lastname is mandatory")
	@NotNull(message = "Lastname is mandatory")
	private String lastname;
	@Email(message = "Email is not well formatted")
	@NotEmpty(message = "Email is mandatory")
	@NotNull(message = "Email is mandatory")
	private String email;
}