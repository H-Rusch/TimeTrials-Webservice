package com.hrusch.timetrials.webservice.exception;

import com.fasterxml.jackson.core.JsonProcessingException;

public class EnumDeserializationException extends JsonProcessingException {

  public EnumDeserializationException(String value, Class<?> clazz) {
    super(String.format("No '%s' can be built from value '%s'.", clazz.getSimpleName(), value));
  }
}
