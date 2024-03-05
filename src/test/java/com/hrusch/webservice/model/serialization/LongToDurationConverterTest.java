package com.hrusch.webservice.model.serialization;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class LongToDurationConverterTest {

  private LongToDurationConverter subject;

  @BeforeEach
  void setUp() {
    subject = new LongToDurationConverter();
  }

  @ParameterizedTest
  @MethodSource("durationSource")
  void givenMillis_whenConvert_thenCorrectDurationReturned(
      Long millis,
      Duration expectedDuration) {
    // given, when & then
    assertThat(subject.convert(millis))
        .isEqualTo(expectedDuration);
  }

  private static Stream<Arguments> durationSource() {
    return Stream.of(
        Arguments.of(1_234L, Duration.parse("PT1.234S")),
        Arguments.of(30_000L, Duration.parse("PT30S")),
        Arguments.of(60_000L, Duration.parse("PT1M")),
        Arguments.of(121_234L, Duration.parse("PT2M1.234S"))
    );
  }
}