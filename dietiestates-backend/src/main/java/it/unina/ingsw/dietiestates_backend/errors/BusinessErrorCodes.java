package it.unina.ingsw.dietiestates_backend.errors;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

public enum BusinessErrorCodes {
	NO_CODE(0, NOT_IMPLEMENTED, "No code"),
	LISTING_NOT_FOUND(404,NOT_FOUND,"Nessun annuncio trovato"),
	INCORRECT_CURRENT_PASSWORD(300, BAD_REQUEST, "La password corrente non è corretta"),
	NEW_PASSWORD_DOES_NOT_MATCH(301, BAD_REQUEST, "La nuova password non corrisponde"),
	ACCOUNT_LOCKED(302, FORBIDDEN, "L'account utente è bloccato"),
	ACCOUNT_DISABLED(303, FORBIDDEN, "L'account utente è disabilitato"),
	BAD_CREDENTIALS(304, FORBIDDEN, "Login e/o Password non corretti"),
	ACCOUNT_NOT_ACTIVATED(305, UNAUTHORIZED, "Account non attivato"),
	AGENT_CANNOT_BE_ADMIN(306, CONFLICT, "Un agente non può avere il ruolo di amministratore"),
	CANNOT_REVOKE_ADMIN(307, FORBIDDEN, "Impossibile revocare il ruolo di amministratore"),
	EMAIL_ALREADY_EXISTS(308, CONFLICT, "L'email è già in uso"),
	INVALID_ACTIVATION_CODE(309, BAD_REQUEST, "Codice di attivazione non valido"),
	INVALID_REFRESH_TOKEN(310, UNAUTHORIZED, "Refresh token non valido"),
	OPERATION_NOT_PERMITTED(311, FORBIDDEN, "Operazione non permessa");

	@Getter
	private final int code;
	@Getter
	private final String description;
	@Getter
	private final HttpStatus httpStatus;

	BusinessErrorCodes(int code, HttpStatus status, String description) {
		this.code = code;
		this.description = description;
		this.httpStatus = status;
	}
}