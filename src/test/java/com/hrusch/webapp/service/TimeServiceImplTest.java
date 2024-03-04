package com.hrusch.webapp.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;

import com.hrusch.webapp.exception.ParameterErrorException;
import com.hrusch.webapp.model.Track;
import com.hrusch.webapp.repository.TimeRepository;
import java.util.stream.Stream;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TimeServiceImplTest {

  @Mock
  TimeRepository timeRepository;

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
}
