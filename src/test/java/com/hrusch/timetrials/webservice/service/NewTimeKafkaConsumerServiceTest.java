package com.hrusch.timetrials.webservice.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.hrusch.timetrials.webservice.exception.TimeDtoValidationException;
import com.hrusch.timetrials.webservice.model.TimeDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import java.util.Collections;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class NewTimeKafkaConsumerServiceTest {

  @Mock
  private TimeService timeService;
  @Mock
  private Validator validator;

  @InjectMocks
  private NewTimeKafkaConsumerService subject;

  @Test
  void givenNewTimeMessage_whenReceivedMessagePassesValidation_thenSaveItToDatabase() {
    // given
    when(validator.validate(any(TimeDto.class)))
        .thenReturn(Collections.emptySet());
    TimeDto timeDto = TimeDto.builder().build();

    // when
    subject.receive(timeDto);

    // then
    verify(timeService).saveNewTime(any(TimeDto.class));
  }

  @Test
  void givenNewTimeMessage_whenReceivedMessageFailsValidation_thenThrowException() {
    // given
    Set<ConstraintViolation<TimeDto>> violations = Set.of(mock(ConstraintViolation.class));
    when(validator.validate(any(TimeDto.class)))
        .thenReturn(violations);
    TimeDto timeDto = TimeDto.builder().build();

    // when & then
    assertThatThrownBy(() -> subject.receive(timeDto))
        .isInstanceOf(TimeDtoValidationException.class);
    verify(timeService, times(0))
        .saveNewTime(any(TimeDto.class));
  }
}