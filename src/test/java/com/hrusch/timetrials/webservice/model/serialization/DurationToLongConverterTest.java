package com.hrusch.timetrials.webservice.model.serialization;


import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class DurationToLongConverterTest {

  private DurationToLongConverter subject;

  @BeforeEach
  void setUp() {
    subject = new DurationToLongConverter();
  }

  @ParameterizedTest
  @MethodSource("durationSource")
  void givenDuration_whenConvert_thenCorrectAmountOfMillisReturned(
      Duration duration,
      Long expectedMillis) {
    // given, when & then
    assertThat(subject.convert(duration))
        .isEqualTo(expectedMillis);
  }

  private static Stream<Arguments> durationSource() {
    return Stream.of(
        Arguments.of(Duration.parse("PT1.234S"), 1_234L),
        Arguments.of(Duration.parse("PT30S"), 30_000L),
        Arguments.of(Duration.parse("PT1M"), 60_000L),
        Arguments.of(Duration.parse("PT2M1.234S"), 121_234L)
    );
  }
}
