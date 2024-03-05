package com.hrusch.webservice.model.serialization;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.hrusch.webservice.exception.DurationDeserializationException;
import java.time.Duration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class CustomDurationDeserializerTest {

  @ParameterizedTest
  @CsvSource({
      "1:07.480, PT1M7.48S",
      "0:57.001, PT57.001S",
      "2:00.000, PT2M",
  })
  void givenCorrectlyFormattedStrings_whenDeserializeDuration_thenReturnCorrectDuration(
      String durationString,
      String isoFormatOfExpected) throws DurationDeserializationException {
    // given
    Duration expectedDuration = Duration.parse(isoFormatOfExpected);

    // when & then
    assertThat(CustomDurationDeserializer.deserializeDuration(durationString))
        .isEqualTo(expectedDuration);
  }

  @Test
  void givenIncorrectlyFormattedString_whenDeserializeDuration_thenThrowDurationDeserializationException() {
    // given
    String durationString = "invalidFormat";

    // when & then
    assertThatThrownBy(() -> CustomDurationDeserializer.deserializeDuration(durationString))
        .isInstanceOf(DurationDeserializationException.class)
        .hasMessage("Duration does not match specified format.");
  }

  @Test
  void givenNull_whenDeserializeDuration_thenThrowDurationDeserializationException() {
    // given
    String durationString = null;

    // when & then
    assertThatThrownBy(() -> CustomDurationDeserializer.deserializeDuration(durationString))
        .isInstanceOf(DurationDeserializationException.class)
        .hasMessage("Duration can not be null.");
  }
}
