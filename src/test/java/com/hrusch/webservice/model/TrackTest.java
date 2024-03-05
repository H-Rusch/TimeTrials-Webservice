package com.hrusch.webservice.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hrusch.webservice.exception.EnumDeserializationException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class TrackTest {

  @ParameterizedTest
  @ValueSource(strings = {"", "Mario"})
  void givenIncorrectString_whenBuildingTrackFromString_thenThrowExceptions(String value) {
    assertThatThrownBy(() -> Track.forValue(value))
        .isInstanceOf(EnumDeserializationException.class)
        .hasMessageStartingWith("No 'Track' can be built from value '");
  }

  @ParameterizedTest
  @ValueSource(strings = {"MARIO_CIRCUIT_DS", "DS Mario Circuit"})
  void givenValidString_whenBuildingTrackFromString_thenReturnCorrectTrack(String value)
      throws JsonProcessingException {
    assertThat(Track.forValue(value))
        .isEqualTo(Track.MARIO_CIRCUIT_DS);
  }
}
