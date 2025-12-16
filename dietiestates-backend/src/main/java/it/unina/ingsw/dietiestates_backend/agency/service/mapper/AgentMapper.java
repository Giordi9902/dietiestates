package it.unina.ingsw.dietiestates_backend.agency.service.mapper;

import it.unina.ingsw.dietiestates_backend.agency.model.dto.CreateAgentRequest;
import it.unina.ingsw.dietiestates_backend.agency.model.dto.AgentResponse;
import it.unina.ingsw.dietiestates_backend.auth.model.User;

public class AgentMapper {
	public static AgentResponse fromEntity(User agent){
		return AgentResponse.builder()
				.id(agent.getId())
				.email(agent.getEmail())
				.username(agent.getUsername())
				.firstName(agent.getFirstname())
				.lastName(agent.getLastname())
				.agencyId(agent.getAgency().getId())
				.agencyName(agent.getAgency().getName())
				.roles(agent.getRoles())
				.build();
	}

	public static User toEntity(CreateAgentRequest req) {
		return User.builder()
				.email(req.getEmail())
				.firstname(req.getFirstname())
				.lastname(req.getLastname())
				.build();
	}
}