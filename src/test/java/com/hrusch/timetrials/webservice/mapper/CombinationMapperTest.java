package com.hrusch.timetrials.webservice.mapper;

import static com.hrusch.timetrials.webservice.model.combination.Driver.FUNKY_KONG;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.hrusch.openapi.model.MkApiCombination;
import com.hrusch.timetrials.webservice.exception.EnumDeserializationException;
import com.hrusch.timetrials.webservice.model.combination.Combination;
import com.hrusch.timetrials.webservice.model.combination.Driver;
import com.hrusch.timetrials.webservice.model.combination.Glider;
import com.hrusch.timetrials.webservice.model.combination.Tires;
import com.hrusch.timetrials.webservice.model.combination.Vehicle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class CombinationMapperTest {

  private static final Tires TIRES = Tires.ROLLER;
  private static final Driver DRIVER = FUNKY_KONG;
  private static final Glider GLIDER = Glider.CLOUD_GLIDER;
  private static final Vehicle VEHICLE = Vehicle.BADWAGON;

  CombinationMapper mapper;

  @BeforeEach
  void setUp() {
    mapper = new CombinationMapperImpl();
  }

  @Nested
  class CombinationMapper_mapTimeRequestToTimeDto_Test {

    @Test
    void givenTimeApiCombination_whenMappingTimeApiCombinationToCombination_thenMappingWorksCorrectly() {
      MkApiCombination combination = validTimeApiCombination();

      Combination result = mapper.map(combination);

      assertThat(result)
          .extracting(
              Combination::getDriver,
              Combination::getVehicle,
              Combination::getTires,
              Combination::getGlider)
          .containsExactly(
              DRIVER,
              VEHICLE,
              TIRES,
              GLIDER);
    }

    @Test
    void givenUnknownDriver_whenMappingTimeApiCombinationToCombination_thenEnumDeserializationExceptionIsThrown() {
      MkApiCombination combination = validTimeApiCombination().driver("unknown");

      assertThatThrownBy(() -> mapper.map(combination))
          .isInstanceOf(EnumDeserializationException.class);
    }

    @Test
    void givenUnknownVehicle_whenMappingTimeApiCombinationToCombination_thenEnumDeserializationExceptionIsThrown() {
      MkApiCombination combination = validTimeApiCombination().vehicle("unknown");

      assertThatThrownBy(() -> mapper.map(combination))
          .isInstanceOf(EnumDeserializationException.class);
    }

    @Test
    void givenUnknownTires_whenMappingTimeApiCombinationToCombination_thenEnumDeserializationExceptionIsThrown() {
      MkApiCombination combination = validTimeApiCombination().tires("unknown");

      assertThatThrownBy(() -> mapper.map(combination))
          .isInstanceOf(EnumDeserializationException.class);
    }

    @Test
    void givenUnknownGlider_whenMappingTimeApiCombinationToCombination_thenEnumDeserializationExceptionIsThrown() {
      MkApiCombination combination = validTimeApiCombination().glider("unknown");

      assertThatThrownBy(() -> mapper.map(combination))
          .isInstanceOf(EnumDeserializationException.class);
    }

    private static MkApiCombination validTimeApiCombination() {
      return new MkApiCombination()
          .driver(DRIVER.getName())
          .vehicle(VEHICLE.name())
          .tires(TIRES.getValue())
          .glider(GLIDER.getValue());
    }
  }

  @Nested
  class CombinationMapper_mapCombinationToTimeApiCombination_Test {

    @Test
    void givenCombination_whenMappingCombinationToTimeApiCombination_thenMappingWorksCorrectly() {
      Combination combination = validCombination();

      MkApiCombination result = mapper.map(combination);

      assertThat(result)
          .extracting(
              MkApiCombination::getDriver,
              MkApiCombination::getVehicle,
              MkApiCombination::getTires,
              MkApiCombination::getGlider)
          .containsExactly(
              DRIVER.getName(),
              VEHICLE.getValue(),
              TIRES.getValue(),
              GLIDER.getValue());
    }

    @Test
    void givenDriverAsNull_whenMappingCombinationToTimeApiCombination_thenNoOutputIsWritten() {
      Combination combination = validCombination();
      combination.setDriver(null);

      var result = mapper.map(combination);

      assertThat(result.getDriver())
          .isNull();
    }

    @Test
    void givenTiresAsNull_whenMappingCombinationToTimeApiCombination_thenNoOutputIsWritten() {
      Combination combination = validCombination();
      combination.setTires(null);

      var result = mapper.map(combination);

      assertThat(result.getTires())
          .isNull();
    }

    @Test
    void givenVehicleAsNull_whenMappingCombinationToTimeApiCombination_thenNoOutputIsWritten() {
      Combination combination = validCombination();
      combination.setVehicle(null);

      var result = mapper.map(combination);

      assertThat(result.getVehicle())
          .isNull();
    }

    @Test
    void givenGliderAsNull_whenMappingCombinationToTimeApiCombination_thenNoOutputIsWritten() {
      Combination combination = validCombination();
      combination.setGlider(null);

      var result = mapper.map(combination);

      assertThat(result.getGlider())
          .isNull();
    }

    private static Combination validCombination() {
      return Combination.builder()
          .tires(TIRES)
          .driver(DRIVER)
          .glider(GLIDER)
          .vehicle(VEHICLE)
          .build();
    }
  }
}