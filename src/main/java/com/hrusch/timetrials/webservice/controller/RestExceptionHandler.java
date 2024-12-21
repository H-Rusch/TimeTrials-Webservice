package com.hrusch.timetrials.webservice.controller;

import com.hrusch.openapi.model.MkApiErrorResponse;
import com.hrusch.openapi.model.MkApiValidationError;
import com.hrusch.timetrials.webservice.exception.TrackDeserializationException;
import java.time.LocalDateTime;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

  @Override
  protected ResponseEntity<Object> handleHttpMessageNotReadable(
      HttpMessageNotReadableException e,
      HttpHeaders headers,
      HttpStatusCode status,
      WebRequest request) {
    return buildResponseEntity(new MkApiErrorResponse()
        .status(HttpStatus.BAD_REQUEST.value())
        .message(e.getMessage()));
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException e,
      HttpHeaders headers,
      HttpStatusCode status,
      WebRequest request) {
    MkApiErrorResponse apiError = buildApiError((HttpStatus) status, "Validation failed");

    e.getBindingResult().getAllErrors().stream()
        .map(this::buildApiValidationErrorFromObjectError)
        .forEach(apiError::addSubErrorsItem);

    return buildResponseEntity(apiError);
  }

  private MkApiValidationError buildApiValidationErrorFromObjectError(ObjectError error) {
    String type = "ValidationError";

    if (error instanceof FieldError fieldError) {
      return new MkApiValidationError()
          .type(type)
          .field(fieldError.getField())
          .rejectedValue(fieldError.getRejectedValue())
          .message(error.getDefaultMessage());
    }

    return new MkApiValidationError()
        .type(type)
        .field(error.getObjectName())
        .message(error.getDefaultMessage());
  }

  // https://stackoverflow.com/questions/36190246/handling-exception-in-spring-boot-rest-thrown-from-custom-converter
  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<Object> handleConverterErrors(
      MethodArgumentTypeMismatchException exception) {
    Throwable cause = exception.getCause()
        .getCause()
        .getCause();

    return buildResponseEntity(buildApiError(HttpStatus.BAD_REQUEST, cause.getMessage()));
  }

  @ExceptionHandler(TrackDeserializationException.class)
  public ResponseEntity<Object> handleTrackDeserializationException(
      TrackDeserializationException ex) {

    return buildResponseEntity(
        buildApiError(HttpStatus.BAD_REQUEST, ex.getException().getMessage()));
  }

  private MkApiErrorResponse buildApiError(HttpStatus status, String message) {
    return new MkApiErrorResponse()
        .status(status.value())
        .message(message)
        .timestamp(LocalDateTime.now());
  }

  private ResponseEntity<Object> buildResponseEntity(MkApiErrorResponse apiError) {
    return ResponseEntity
        .status(apiError.getStatus())
        .body(apiError);
  }
}
