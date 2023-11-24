package com.hrusch.webapp.error.exception;

import com.fasterxml.jackson.core.JsonProcessingException;

public class TrackDeserializationException extends JsonProcessingException {
    public TrackDeserializationException(String msg) {
        super(msg);
    }
}
