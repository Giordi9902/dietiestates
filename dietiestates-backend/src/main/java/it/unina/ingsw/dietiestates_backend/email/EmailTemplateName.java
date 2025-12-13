package it.unina.ingsw.dietiestates_backend.email;

import lombok.Getter;

@Getter
public enum EmailTemplateName {

	ACTIVATE_ACCOUNT("activate_account"),
	AGENT_CREATED("agent_created"),
	AGENT_PROMOTED("agent_promoted"),
	PASSWORD_CHANGED("password_changed"),
	AGENT_ROLE_ADDED("agent_role_added");


	private final String templateName;

	EmailTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public String getTemplateName() {
		return templateName;
	}
}