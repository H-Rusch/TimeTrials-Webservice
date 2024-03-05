package com.hrusch.timetrials.webservice.model.combination;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hrusch.timetrials.webservice.exception.EnumDeserializationException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class TiresTest {

    @ParameterizedTest
    @ValueSource(strings = {"", "Wheelz"})
    void givenIncorrectString_whenBuildingTiresFromString_thenThrowExceptions(String value) {
        assertThatThrownBy(() -> Tires.forValue(value))
                .isInstanceOf(EnumDeserializationException.class)
                .hasMessageStartingWith("No 'Tires' can be built from value '");
    }

    @ParameterizedTest
    @ValueSource(strings = {"AZURE_ROLLER", "Azure Roller"})
    void givenValidString_whenBuildingTiresFromString_thenReturnCorrectTires(String value) throws JsonProcessingException {
        assertThat(Tires.forValue(value)).isEqualTo(Tires.AZURE_ROLLER);
    }
}
