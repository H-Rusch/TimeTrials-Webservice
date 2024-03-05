package com.hrusch.webservice.controller;

import com.hrusch.webservice.controller.response.ApiError;
import com.hrusch.webservice.controller.response.ApiValidationError;
import com.hrusch.webservice.exception.ParameterErrorException;
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
    return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, e.getMessage()));
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException e,
      HttpHeaders headers,
      HttpStatusCode status,
      WebRequest request) {
    var apiError = new ApiError((HttpStatus) status, "Validation failed");
    e.getBindingResult().getAllErrors().stream()
        .map(this::buildApiValidationErrorFromObjectError)
        .forEach(apiError::addSubError);

    return buildResponseEntity(apiError);
  }

  private ApiValidationError buildApiValidationErrorFromObjectError(ObjectError error) {
    String field =
        error instanceof FieldError fieldError ? fieldError.getField() : error.getObjectName();
    Object rejectedValue =
        error instanceof FieldError fieldError ? fieldError.getRejectedValue() : null;

    return new ApiValidationError(field, rejectedValue, error.getDefaultMessage());
  }

  @ExceptionHandler(ParameterErrorException.class)
  public ResponseEntity<Object> handleParameterErrorException(ParameterErrorException exception) {
    return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, exception.getMessage()));
  }

  // https://stackoverflow.com/questions/36190246/handling-exception-in-spring-boot-rest-thrown-from-custom-converter
  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<Object> handleConverterErrors(
      MethodArgumentTypeMismatchException exception) {
    Throwable cause = exception.getCause()
        .getCause()
        .getCause();

    return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, cause.getMessage()));
  }

  private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
    return ResponseEntity
        .status(apiError.getStatus())
        .body(apiError);
  }
}
