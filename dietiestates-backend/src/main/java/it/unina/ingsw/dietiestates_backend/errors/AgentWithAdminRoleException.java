package it.unina.ingsw.dietiestates_backend.errors;

public class AgentWithAdminRoleException extends RuntimeException{
	public AgentWithAdminRoleException(String message){super(message);}
}