package com.hrusch.timetrials.webservice.exception;

import static org.assertj.core.api.Assertions.assertThat;

import com.hrusch.timetrials.webservice.model.TimeDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TimeDtoValidationExceptionTest {

  private Validator validator;

  @BeforeEach
  void setUp() {
    try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
      validator = factory.getValidator();
    }
  }

  @Test
  void givenConstraintViolationsFromInvalidObject_whenConstructingException_thenExceptionCorrectlySetUp() {
    // given
    TimeDto invalidTimeDto = buildInvalidTimeDto();
    Set<ConstraintViolation<TimeDto>> constraintViolations = validator.validate(invalidTimeDto);

    // when
    TimeDtoValidationException exception = new TimeDtoValidationException(constraintViolations);

    assertThat(exception.getConstraintViolations())
        .isEqualTo(constraintViolations);
    assertThat(exception.getMessage())
        .contains("duration: ", "track: ", "username: ");
  }

  private static TimeDto buildInvalidTimeDto() {
    return TimeDto.builder()
        .username("a")
        .build();
  }
}