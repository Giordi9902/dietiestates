package it.unina.ingsw.dietiestates_backend.errors;

public class AccountNotActivatedException extends RuntimeException {
    public AccountNotActivatedException(String message) {
        super(message);
    }
}