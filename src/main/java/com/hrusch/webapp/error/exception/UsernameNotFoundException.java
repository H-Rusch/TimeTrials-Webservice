package com.hrusch.webapp.error.exception;

public class UsernameNotFoundException extends UserDoesNotExistException {

    public UsernameNotFoundException(String value) {
        super("username", value);
    }
}
