package com.hrusch.timetrials.webservice.model.combination;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hrusch.timetrials.webservice.config.JacksonConfig;
import com.hrusch.timetrials.webservice.util.TestDataReader;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class CombinationTest {

  private static final String DIRECTORY = "model";

  private final ObjectMapper objectMapper = new JacksonConfig().objectMapper();

  private final Combination subject = new Combination(
      Driver.FUNKY_KONG,
      Vehicle.BADWAGON,
      Tires.CYBER_SLICK,
      Glider.BOWSER_KITE);

  @Test
  void givenCombinationObject_whenSerializing_produceCorrectJson() throws JsonProcessingException {
    // given
    String expected = TestDataReader.readFileToString(DIRECTORY, "combination_valid.json");

    // when
    String json = objectMapper.writeValueAsString(subject);

    // then
    assertThat(json).isEqualTo(expected);
  }

  @Test
  void givenValidCombinationJson_whenDeserializing_produceCorrectObject()
      throws JsonProcessingException {
    // given
    String json = TestDataReader.readFileToString(DIRECTORY, "combination_valid.json");

    // when
    Combination combination = objectMapper.readValue(json, Combination.class);

    // then
    assertThat(combination).isEqualTo(subject);
  }

  @ParameterizedTest
  @ValueSource(strings = {
      "combination_invalid_bad_driver.json",
      "combination_invalid_bad_glider.json",
      "combination_invalid_bad_tires.json",
      "combination_invalid_bad_vehicle.json"
  })
  void givenInvalidCombinationJson_whenDeserializing_throwException(String file) {
    // given
    String json = TestDataReader.readFileToString(DIRECTORY, file);

    // when & then
    assertThatThrownBy(() -> objectMapper.readValue(json, Combination.class))
        .isInstanceOf(JsonProcessingException.class);
  }

  @Test
  void givenIncompleteCombination_whenValidation_returnValidationErrors() {
    // given
    Combination combination = new Combination();
    Validator validator;
    try (var validatorFactory = Validation.buildDefaultValidatorFactory()) {
      validator = validatorFactory.getValidator();
    }

    // when
    Set<ConstraintViolation<Combination>> violations = validator.validate(combination);

    // then
    assertThat(violations).hasSize(4);
  }
}
