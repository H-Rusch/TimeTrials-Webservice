package com.hrusch.timetrials.webservice.model.combination;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hrusch.timetrials.webservice.exception.EnumDeserializationException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class DriverTest {
    
    @ParameterizedTest
    @ValueSource(strings = {"", "Sonic"})
    void givenIncorrectString_whenBuildingDriverFromString_thenThrowExceptions(String value) {
        assertThatThrownBy(() -> Driver.forValue(value))
                .isInstanceOf(EnumDeserializationException.class)
                .hasMessageStartingWith("No 'Driver' can be built from value '");
    }

    @ParameterizedTest
    @ValueSource(strings = {"BOWSER_JR", "Bowser Jr."})
    void givenValidString_whenBuildingDriverFromString_thenReturnCorrectDriver(String value) throws JsonProcessingException {
        assertThat(Driver.forValue(value)).isEqualTo(Driver.BOWSER_JR);
    }
}
