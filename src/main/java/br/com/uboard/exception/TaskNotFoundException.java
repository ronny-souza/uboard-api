package br.com.uboard.exception;

public class TaskNotFoundException extends UboardApplicationException {

    public TaskNotFoundException(String message) {
        super(message);
    }
}
