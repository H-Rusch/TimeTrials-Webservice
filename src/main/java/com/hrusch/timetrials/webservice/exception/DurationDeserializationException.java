package com.hrusch.timetrials.webservice.exception;

import com.fasterxml.jackson.core.JsonProcessingException;

public class DurationDeserializationException extends JsonProcessingException {

  public DurationDeserializationException(String msg) {
    super(msg);
  }
}
