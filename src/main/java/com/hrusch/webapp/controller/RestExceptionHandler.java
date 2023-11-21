package com.hrusch.webapp.controller;

import com.hrusch.webapp.exception.UserDoesNotExistException;
import com.hrusch.webapp.exception.UsernameAlreadyTakenException;
import com.hrusch.webapp.model.errorResponse.ApiError;
import com.hrusch.webapp.model.errorResponse.ApiValidationError;
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
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


// https://www.toptal.com/java/spring-boot-rest-api-error-handling
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException e, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, e.getMessage()));
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        var apiError = new ApiError((HttpStatus) status, "Validation failed");
        e.getBindingResult().getAllErrors().stream()
                .map(this::buildApiValidationErrorFromObjectError)
                .forEach(apiError::addSubError);

        return buildResponseEntity(apiError);
    }

    private ApiValidationError buildApiValidationErrorFromObjectError(ObjectError error) {
        String field = error instanceof FieldError fieldError ? fieldError.getField() : error.getObjectName();
        Object rejectedValue = error instanceof FieldError fieldError ? fieldError.getRejectedValue() : null;

        return new ApiValidationError(field, rejectedValue, error.getDefaultMessage());
    }

    @ExceptionHandler({UsernameAlreadyTakenException.class, UserDoesNotExistException.class})
    public ResponseEntity<Object> handleException(Exception e) {
        return buildResponseEntity(new ApiError(HttpStatus.CONFLICT, e.getMessage()));
    }

    private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
        return ResponseEntity
                .status(apiError.getStatus())
                .body(apiError);
    }
}
