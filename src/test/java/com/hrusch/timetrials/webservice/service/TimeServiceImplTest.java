package com.hrusch.timetrials.webservice.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.hrusch.timetrials.webservice.exception.ParameterErrorException;
import com.hrusch.timetrials.webservice.model.Time;
import com.hrusch.timetrials.webservice.model.TimeDto;
import com.hrusch.timetrials.webservice.model.Track;
import com.hrusch.timetrials.webservice.repository.TimeRepository;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.stream.Stream;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

@ExtendWith(MockitoExtension.class)
class TimeServiceImplTest {

  private static final String USERNAME = "username";
  private static final Track TRACK = Track.BABY_PARK_GCN;
  private static final Duration DURATION = Duration.parse("PT1M5,48S");
  private static final LocalDateTime CREATED_AT = LocalDateTime.now();

  @Mock
  ModelMapper modelMapper;
  @Mock
  TimeRepository timeRepository;
  @Mock
  RecordKeeperService recordKeeperService;

  @InjectMocks
  TimeServiceImpl subject;

  @Nested
  class TimeServiceImplGetBestTimeForEachTrackTest {

    @ParameterizedTest
    @MethodSource("usernameParameterVariations")
    void givenUsername_whenGetBestTimeForEachTrack_thenRepositoryMethodCalled(String username) {
      // given & when
      subject.getBestTimeForEachTrack(username);

      // then
      verify(timeRepository).findBestTimeForEachTrack(username);
    }

    private static Stream<String> usernameParameterVariations() {
      return Stream.of(null, "", "username");
    }
  }

  @Nested
  class TimeServiceImplGetBestTimeForTrackTest {

    @ParameterizedTest
    @MethodSource("usernameParameterVariations")
    void givenValidParameters_whenGetBestTimeForTrack_thenRepositoryMethodCalled(String username) {
      // given
      Track track = Track.BABY_PARK_GCN;

      // when
      subject.getBestTimeForTrack(track, username);

      // then
      verify(timeRepository).findBestTimeForTrack(track, username);
    }


    @ParameterizedTest
    @MethodSource("usernameParameterVariations")
    void givenNullAsTrack_whenGetBestTimeForTrack_thenException(String username) {
      // given, when & then
      assertThatThrownBy(() -> subject.getBestTimeForTrack(null, username))
          .isInstanceOf(ParameterErrorException.class)
          .hasMessage("The required parameter 'track' contains an error.");
    }

    private static Stream<String> usernameParameterVariations() {
      return Stream.of(null, "", "username");
    }
  }

  @Nested
  class TimeServiceImplSaveTime {

    @Test
    void givenTimeDto_whenSaveTime_thenCorrectMethodsCalled() {
      // given
      TimeDto timeDto = createSampleTimeDto();
      Time timeToSave = createSampleTime(null);
      Time savedTime = createSampleTime("id");

      when(modelMapper.map(timeDto, Time.class))
          .thenReturn(timeToSave);
      when(timeRepository.saveTime(any(Time.class)))
          .thenReturn(savedTime);

      // when
      subject.saveNewTime(timeDto);

      // then
      verify(timeRepository).saveTime(timeToSave);
      verify(recordKeeperService).update(savedTime);
    }
  }

  private static TimeDto createSampleTimeDto() {
    return TimeDto.builder()
        .username(USERNAME)
        .track(TRACK)
        .duration(DURATION)
        .build();
  }

  private static Time createSampleTime(String id) {
    return Time.builder()
        .id(id)
        .username(USERNAME)
        .track(TRACK)
        .duration(DURATION)
        .createdAt(CREATED_AT)
        .build();
  }
}
