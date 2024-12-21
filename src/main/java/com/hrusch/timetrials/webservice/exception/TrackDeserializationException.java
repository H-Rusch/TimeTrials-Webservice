package com.hrusch.timetrials.webservice.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class TrackDeserializationException extends RuntimeException {

  private final JsonProcessingException exception;
}
