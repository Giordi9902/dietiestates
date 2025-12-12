package it.unina.ingsw.dietiestates_backend.errors;

public class OperationNotPermittedException extends RuntimeException {
	public OperationNotPermittedException(String message) {
		super(message);
	}
}