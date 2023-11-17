package com.hrusch.webapp.exception;

public class UserDoesNotExistException extends Exception {

    public UserDoesNotExistException(String userId) {
        super(String.format("User with the userId %s does not exist.", userId));
    }
}
