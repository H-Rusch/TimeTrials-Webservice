package com.hrusch.timetrials.webservice.exception;

import com.hrusch.timetrials.webservice.model.TimeDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ValidationException;
import java.util.Optional;
import java.util.Set;
import lombok.Getter;

@Getter
public class TimeDtoValidationException extends ValidationException {

  private final transient Set<ConstraintViolation<TimeDto>> constraintViolations;

  public TimeDtoValidationException(Set<ConstraintViolation<TimeDto>> constraintViolations) {
    super(combineToPrettyString(constraintViolations));
    this.constraintViolations = constraintViolations;
  }

  private static String combineToPrettyString(
      Set<ConstraintViolation<TimeDto>> constraintViolations) {
    return String.join(", ", constraintViolations.stream()
        .map(TimeDtoValidationException::convertToPrettyString)
        .toList());
  }

  private static String convertToPrettyString(ConstraintViolation<TimeDto> constraintViolation) {
    return Optional.ofNullable(constraintViolation.getPropertyPath())
        .map(path -> String.format("%s: %s", path, constraintViolation.getMessage()))
        .orElse(constraintViolation.getMessage());
  }
}
