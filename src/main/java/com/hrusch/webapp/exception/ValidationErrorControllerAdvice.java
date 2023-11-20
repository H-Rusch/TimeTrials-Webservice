package com.hrusch.webapp.exception;

import com.hrusch.webapp.model.response.ValidationError;
import com.hrusch.webapp.model.response.ValidationErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@ControllerAdvice
public class ValidationErrorControllerAdvice {

    /*@ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    ValidationErrorResponse handleConstraintViolationException(
            ConstraintViolationException e) {

        ValidationErrorResponse errorResponse = new ValidationErrorResponse();

        for (var violation : e.getConstraintViolations()) {
            errorResponse.getViolations().add(
                    new ValidationError(violation.getPropertyPath().toString(),
                            violation.getMessage())
            );
        }

        return errorResponse;
    }*/

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    ValidationErrorResponse handleMethodArgumentNotValidException(
            MethodArgumentNotValidException e) {
        List<ValidationError> violations = e.getBindingResult().getAllErrors().stream()
                .map(this::buildValidationErrorFromObjectError)
                .toList();

        return new ValidationErrorResponse(violations);
    }

    private ValidationError buildValidationErrorFromObjectError(ObjectError error) {
        String field = error instanceof FieldError fieldError ? fieldError.getField() : error.getObjectName();

        return new ValidationError(field, error.getDefaultMessage());
    }
}
