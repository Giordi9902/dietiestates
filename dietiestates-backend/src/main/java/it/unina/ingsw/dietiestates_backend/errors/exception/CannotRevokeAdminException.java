package it.unina.ingsw.dietiestates_backend.errors.exception;

public class CannotRevokeAdminException extends RuntimeException {
	public CannotRevokeAdminException(String message) {
		super(message);
	}
}