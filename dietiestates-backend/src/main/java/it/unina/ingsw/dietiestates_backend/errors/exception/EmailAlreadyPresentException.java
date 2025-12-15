package it.unina.ingsw.dietiestates_backend.errors.exception;

public class EmailAlreadyPresentException extends RuntimeException {
	public EmailAlreadyPresentException(String message) {
		super(message);
	}
}