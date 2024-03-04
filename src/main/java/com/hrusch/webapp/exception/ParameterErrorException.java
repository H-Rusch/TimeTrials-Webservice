package com.hrusch.webapp.exception;

public class ParameterErrorException extends RuntimeException {

  public ParameterErrorException(String parameterContainingError) {
    super(String.format("The required parameter '%s' contains an error.", parameterContainingError));
  }
}