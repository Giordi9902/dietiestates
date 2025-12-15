package it.unina.ingsw.dietiestates_backend.errors.exception;

public class InvalidActivationCodeException extends RuntimeException {
    public InvalidActivationCodeException(String message) {
        super(message);
    }
}