package com.hrusch.webapp.exception;

public class UsernameAlreadyTakenException extends Exception {

    private static final String MESSAGE = "The username has already been taken: %s";

    public UsernameAlreadyTakenException(String username) {
        super(String.format(MESSAGE, username));
    }
}
