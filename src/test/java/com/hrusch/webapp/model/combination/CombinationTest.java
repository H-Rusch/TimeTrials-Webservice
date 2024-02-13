package com.hrusch.webapp.model.combination;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hrusch.webapp.config.JacksonConfig;
import com.hrusch.webapp.util.FileReader;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class CombinationTest {

    private final FileReader fileReader = new FileReader();
    private final ObjectMapper objectMapper = new JacksonConfig().objectMapper();

    private final Combination subject = new Combination(Driver.FUNKY_KONG, Vehicle.BADWAGON, Tires.CYBER_SLICK, Glider.BOWSER_KITE);

    @Test
    void givenCombinationObject_whenSerializing_produceCorrectJson() throws JsonProcessingException {
        String expected = fileReader.readFileToString("combination_valid.json");

        String json = objectMapper.writeValueAsString(subject);

        assertThat(json).isEqualTo(expected);
    }

    @Test
    void givenValidCombinationJson_whenDeserializing_produceCorrectObject() throws JsonProcessingException {
        String json = fileReader.readFileToString("combination_valid.json");

        Combination combination = objectMapper.readValue(json, Combination.class);

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
        String json = fileReader.readFileToString(file);

        assertThatThrownBy(() -> objectMapper.readValue(json, Combination.class))
                .isInstanceOf(JsonProcessingException.class);
    }

    @Test
    void givenIncompleteCombination_whenValidation_returnValidationErrors() {
        var combination = new Combination();
        Validator validator;
        try (var validatorFactory = Validation.buildDefaultValidatorFactory()) {
            validator = validatorFactory.getValidator();
        }

        Set<ConstraintViolation<Combination>> violations = validator.validate(combination);

        assertThat(violations).hasSize(4);
    }
}
