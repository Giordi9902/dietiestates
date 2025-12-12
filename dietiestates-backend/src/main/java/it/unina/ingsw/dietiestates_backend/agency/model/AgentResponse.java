package it.unina.ingsw.dietiestates_backend.agency.model;

import it.unina.ingsw.dietiestates_backend.auth.model.Role;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AgentResponse {
	private Integer id;
	private String email;
	private String username;
	private String firstName;
	private String lastName;
	private Integer agencyId;
	private String agencyName;
	private List<Role> roles;
}