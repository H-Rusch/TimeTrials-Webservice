package com.hrusch.webapp.exception;

public class UserIdNotFoundException extends UserDoesNotExistException {

    public UserIdNotFoundException(String value) {
        super("userId", value);
    }
}
