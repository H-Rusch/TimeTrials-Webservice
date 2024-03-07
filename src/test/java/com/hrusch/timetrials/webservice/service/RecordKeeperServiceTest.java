package com.hrusch.timetrials.webservice.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.hrusch.timetrials.webservice.model.Time;
import com.hrusch.timetrials.webservice.model.Track;
import com.hrusch.timetrials.webservice.repository.TimeRepository;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RecordKeeperServiceTest {

  private static final Duration DURATION = Duration.parse("PT1M5.48S");

  @Mock
  private TimeRepository timeRepository;
  @Mock
  private PublishNewRecordKafkaProducerService publishNewRecordKafkaProducerService;

  @InjectMocks
  private RecordKeeperService subject;

  @Test
  void givenTime_whenUpdatingNoTimeExists_thenVerifyInteraction() {
    // given
    Time time = createValidTime(DURATION);

    // when
    subject.update(time);

    // then
    verify(publishNewRecordKafkaProducerService)
        .publish(time);
  }

  @ParameterizedTest
  @MethodSource("durationsWhichCauseUpdatingSource")
  void givenTime_whenUpdatingAndNewDurationCausesUpdate_thenVerifyInteraction(Duration duration) {
    // given
    when(timeRepository.findBestTimeForEachTrack(null))
        .thenReturn(Set.of(createValidTime(DURATION)));
    subject = new RecordKeeperService(timeRepository, publishNewRecordKafkaProducerService);
    Time time = createValidTime(duration);

    // when
    subject.update(time);

    // then
    verify(publishNewRecordKafkaProducerService)
        .publish(time);
  }

  private static Stream<Duration> durationsWhichCauseUpdatingSource() {
    return Stream.of(
        DURATION,
        DURATION.minus(Duration.ofMillis(1)));
  }

  @Test
  void givenTime_whenUpdatingAndTimeWorseThanExisting_thenVerifyNoInteraction() {
    // given
    when(timeRepository.findBestTimeForEachTrack(null))
        .thenReturn(Set.of(createValidTime(DURATION)));
    subject = new RecordKeeperService(timeRepository, publishNewRecordKafkaProducerService);
    Time time = createValidTime(DURATION.plus(Duration.ofMillis(1)));

    // when
    subject.update(time);

    // then
    verify(publishNewRecordKafkaProducerService, never())
        .publish(any());
  }

  @ParameterizedTest
  @MethodSource("invalidTimeSource")
  void givenInvalidTime_whenUpdating_thenVerifyNoInteraction(Time invalidTime) {
    // given & when
    subject.update(invalidTime);

    // then
    verify(publishNewRecordKafkaProducerService, never())
        .publish(any());
  }

  private static Stream<Time> invalidTimeSource() {
    return Stream.of(
        null,
        Time.builder().build()
    );
  }

  private static Time createValidTime(Duration duration) {
    return Time.builder()
        .username("username")
        .track(Track.BABY_PARK_GCN)
        .duration(duration)
        .createdAt(LocalDateTime.now())
        .build();
  }
}