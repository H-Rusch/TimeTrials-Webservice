package com.hrusch.webservice.model.combination;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hrusch.webservice.exception.EnumDeserializationException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class GliderTest {
  @ParameterizedTest
  @ValueSource(strings = {"", "Kart"})
  void givenIncorrectString_whenBuildingGliderFromString_thenThrowExceptions(String value) {
    assertThatThrownBy(() -> Glider.forValue(value))
        .isInstanceOf(EnumDeserializationException.class)
        .hasMessageStartingWith("No 'Glider' can be built from value '");
  }

  @ParameterizedTest
  @ValueSource(strings = {"GOLD_GLIDER", "Gold Glider"})
  void givenValidString_whenBuildingGliderFromString_thenReturnCorrectGlider(String value)
      throws JsonProcessingException {
    assertThat(Glider.forValue(value)).isEqualTo(Glider.GOLD_GLIDER);
  }
}
