package com.hrusch.webapp.exception;

import com.fasterxml.jackson.core.JsonProcessingException;

public class DurationDeserializationException extends JsonProcessingException {

  public DurationDeserializationException(String msg) {
    super(msg);
  }
}
