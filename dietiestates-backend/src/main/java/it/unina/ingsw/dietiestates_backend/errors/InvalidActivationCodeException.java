package it.unina.ingsw.dietiestates_backend.errors;

public class InvalidActivationCodeException extends RuntimeException {
    public InvalidActivationCodeException(String message) {
        super(message);
    }
}