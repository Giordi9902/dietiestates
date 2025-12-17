package it.unina.ingsw.dietiestates_backend.errors.exception;

public class UserIsAlreadyAgentException extends RuntimeException {
    public UserIsAlreadyAgentException(String message) {
        super(message);
    }
}
