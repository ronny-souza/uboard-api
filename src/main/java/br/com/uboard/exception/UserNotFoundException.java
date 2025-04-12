package br.com.uboard.exception;

public class UserNotFoundException extends UboardApplicationException {

    public UserNotFoundException(String message) {
        super(message);
    }
}
