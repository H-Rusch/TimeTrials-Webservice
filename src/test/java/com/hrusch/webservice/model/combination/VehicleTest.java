package com.hrusch.webservice.model.combination;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hrusch.webservice.exception.EnumDeserializationException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class VehicleTest {

    @ParameterizedTest
    @ValueSource(strings = {"", "Kart"})
    void givenIncorrectString_whenBuildingVehicleFromString_thenThrowExceptions(String value) {
        assertThatThrownBy(() -> Vehicle.forValue(value))
                .isInstanceOf(EnumDeserializationException.class)
                .hasMessageStartingWith("No 'Vehicle' can be built from value '");
    }

    @ParameterizedTest
    @ValueSource(strings = {"W_25_SILVER_ARROW", "W 25 Silver Arrow"})
    void givenValidString_whenBuildingVehicleFromString_thenReturnCorrectVehicle(String value) throws JsonProcessingException {
        assertThat(Vehicle.forValue(value)).isEqualTo(Vehicle.W_25_SILVER_ARROW);
    }
}
