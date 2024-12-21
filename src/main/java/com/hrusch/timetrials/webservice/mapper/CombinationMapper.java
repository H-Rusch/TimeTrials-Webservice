package com.hrusch.timetrials.webservice.mapper;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

import com.hrusch.openapi.model.MkApiCombination;
import com.hrusch.timetrials.webservice.model.combination.Combination;
import com.hrusch.timetrials.webservice.model.combination.Driver;
import com.hrusch.timetrials.webservice.model.combination.Glider;
import com.hrusch.timetrials.webservice.model.combination.Tires;
import com.hrusch.timetrials.webservice.model.combination.Vehicle;
import java.util.Optional;
import lombok.SneakyThrows;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = SPRING)
public interface CombinationMapper {

  @Mapping(source = "driver", target = "driver")
  @Mapping(source = "vehicle", target = "vehicle")
  @Mapping(source = "tires", target = "tires")
  @Mapping(source = "glider", target = "glider")
  Combination map(MkApiCombination combination);

  @Mapping(source = "driver", target = "driver", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
      nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  @Mapping(source = "vehicle", target = "vehicle", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
      nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  @Mapping(source = "tires", target = "tires", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
      nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  @Mapping(source = "glider", target = "glider", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
      nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  MkApiCombination map(Combination combination);

  @SneakyThrows
  default Driver mapToDriver(String driverName) {
    return Driver.forValue(driverName);
  }

  @SneakyThrows
  default Vehicle mapToVehicle(String vehicleName) {
    return Vehicle.forValue(vehicleName);
  }

  @SneakyThrows
  default Tires mapToTires(String tiresName) {
    return Tires.forValue(tiresName);
  }

  @SneakyThrows
  default Glider mapToGlider(String gliderName) {
    return Glider.forValue(gliderName);
  }

  default String mapToString(Driver driver) {
    return Optional.ofNullable(driver)
        .map(Driver::getName)
        .orElse(null);
  }

  default String mapToString(Vehicle vehicle) {
    return vehicle.getValue();
  }

  default String mapToString(Tires tires) {
    return tires.getValue();
  }

  default String mapToString(Glider glider) {
    return glider.getValue();
  }
}
