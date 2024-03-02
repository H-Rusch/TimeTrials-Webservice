package com.hrusch.webapp.model.serialization;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import org.junit.jupiter.api.Test;

class CustomDurationSerializerTest {

  @Test
  void givenDuration_whenSerializing_thenDurationIsWrittenInCorrectFormat() {
    // given
    Duration duration = Duration.parse("PT1M7.48S");
    String expectedString = "1:07.480";

    // then & when
    assertThat(CustomDurationSerializer.serializeDuration(duration))
        .isEqualTo(expectedString);
  }

}