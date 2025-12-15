package it.unina.ingsw.dietiestates_backend.errors.exception;

public class OperationNotPermittedException extends RuntimeException {
	public OperationNotPermittedException(String message) {
		super(message);
	}
}