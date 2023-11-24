package com.hrusch.webapp.error.exception;

public class UserIdNotFoundException extends UserDoesNotExistException {

    public UserIdNotFoundException(String value) {
        super("userId", value);
    }
}
