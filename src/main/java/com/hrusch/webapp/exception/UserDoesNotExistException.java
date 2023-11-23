package com.hrusch.webapp.exception;

public abstract class UserDoesNotExistException extends Exception {

    UserDoesNotExistException(String identifyingField, String value) {
        super(String.format("User with the %s '%s' does not exist.", identifyingField, value));
    }
}
