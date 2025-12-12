package it.unina.ingsw.dietiestates_backend.agency.model;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AgencyDetailsResponse {
	private Integer id;
	private String name;
	private String phoneNumber;
	private String email;
}