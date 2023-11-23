package com.hrusch.webapp.exception;

public class UsernameNotFoundException extends UserDoesNotExistException {

    public UsernameNotFoundException(String value) {
        super("username", value);
    }
}
