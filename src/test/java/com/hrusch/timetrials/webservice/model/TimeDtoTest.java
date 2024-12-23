package com.hrusch.timetrials.webservice.model;

import static org.assertj.core.api.Assertions.assertThat;

import com.hrusch.timetrials.webservice.model.combination.Combination;
import com.hrusch.timetrials.webservice.model.combination.Driver;
import com.hrusch.timetrials.webservice.model.combination.Glider;
import com.hrusch.timetrials.webservice.model.combination.Tires;
import com.hrusch.timetrials.webservice.model.combination.Vehicle;
import java.time.Duration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

class TimeDtoTest {

  private ModelMapper modelMapper;

  @BeforeEach
  void setUp() {
    this.modelMapper = new ModelMapper();
  }

  @Test
  void givenTimeDto_whenMappingToTime_thenSameValuesAfterMapping() {
    // given
    TimeDto timeDto = createTimeDto();

    // when
    Time time = modelMapper.map(timeDto, Time.class);

    // then
    assertThat(time)
        .extracting(
            Time::getId,
            Time::getUsername,
            Time::getTrack,
            Time::getDuration,
            Time::getCreatedAt,
            Time::getCombination)
        .containsExactly(
            null,
            timeDto.getUsername(),
            timeDto.getTrack(),
            timeDto.getDuration(),
            null,
            timeDto.getCombination());
  }

  @Test
  void givenTimeDtoWithoutCombination_whenMappingToTime_thenSameValuesAfterMapping() {
    // given
    TimeDto timeDto = createTimeDto();
    timeDto.setCombination(null);

    // when
    Time time = modelMapper.map(timeDto, Time.class);

    // then
    assertThat(time)
        .extracting(
            Time::getId,
            Time::getUsername,
            Time::getTrack,
            Time::getDuration,
            Time::getCreatedAt,
            Time::getCombination)
        .containsExactly(
            null,
            timeDto.getUsername(),
            timeDto.getTrack(),
            timeDto.getDuration(),
            null,
            null);
  }

  private static TimeDto createTimeDto() {
    return TimeDto.builder()
        .username("username")
        .track(Track.BABY_PARK_GCN)
        .duration(Duration.parse("PT1M7.48S"))
        .combination(Combination.builder()
            .driver(Driver.FUNKY_KONG)
            .vehicle(Vehicle.BADWAGON)
            .tires(Tires.ROLLER)
            .glider(Glider.FLOWER_GLIDER)
            .build())
        .build();
  }
}
