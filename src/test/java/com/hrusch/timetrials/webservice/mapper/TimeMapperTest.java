package com.hrusch.timetrials.webservice.mapper;

import static com.hrusch.timetrials.webservice.model.Track.MARIO_CIRCUIT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.hrusch.openapi.model.MkApiCombination;
import com.hrusch.openapi.model.MkApiTimeRequest;
import com.hrusch.openapi.model.MkApiTimeResponse;
import com.hrusch.timetrials.webservice.exception.DurationDeserializationException;
import com.hrusch.timetrials.webservice.exception.EnumDeserializationException;
import com.hrusch.timetrials.webservice.model.Time;
import com.hrusch.timetrials.webservice.model.TimeDto;
import com.hrusch.timetrials.webservice.model.Track;
import com.hrusch.timetrials.webservice.model.combination.Combination;
import com.hrusch.timetrials.webservice.model.combination.Driver;
import com.hrusch.timetrials.webservice.model.combination.Glider;
import com.hrusch.timetrials.webservice.model.combination.Tires;
import com.hrusch.timetrials.webservice.model.combination.Vehicle;
import com.hrusch.timetrials.webservice.model.serialization.CustomDurationSerializer;
import java.time.Duration;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TimeMapperTest {

  private static final String USERNAME = "Username";
  private static final Track TRACK = MARIO_CIRCUIT;
  private static final Duration DURATION = Duration.parse("PT1M4.78S");
  private static final Combination COMBINATION = Combination.builder()
      .vehicle(Vehicle.BADWAGON)
      .glider(Glider.CLOUD_GLIDER)
      .driver(Driver.FUNKY_KONG)
      .tires(Tires.ROLLER)
      .build();
  private static final LocalDateTime TIMESTAMP = LocalDateTime.now();

  @Mock
  CombinationMapper combinationMapper;

  @InjectMocks
  TimeMapperImpl mapper;

  @Nested
  class TimeMapper_mapMkApiTimeRequestToTimeDto_Test {

    @Test
    void givenValidRequest_whenMappingTimeRequestToTimeDto_thenMappingWorksCorrectly() {
      MkApiTimeRequest request = validTimeRequest();
      when(combinationMapper.map(any(MkApiCombination.class)))
          .thenReturn(COMBINATION);

      TimeDto result = mapper.map(request);

      assertThat(result)
          .extracting(
              TimeDto::getUsername,
              TimeDto::getTrack,
              TimeDto::getDuration,
              TimeDto::getCombination,
              TimeDto::getCreatedAt
          )
          .containsExactly(
              USERNAME,
              TRACK,
              DURATION,
              COMBINATION,
              null);
    }

    @Test
    void givenNoCombination_whenMappingTimeRequestToTimeDto_thenResultShouldHaveNoCombination() {
      MkApiTimeRequest request = validTimeRequest().combination(null);

      TimeDto result = mapper.map(request);

      assertThat(result.getCombination()).isNull();
    }

    @Test
    void givenUnknownTrackName_whenMappingTimeRequestToTimeDto_thenEnumDeserializationExceptionIsThrown() {
      MkApiTimeRequest request = validTimeRequest().track("unknown");

      assertThatThrownBy(() -> mapper.map(request))
          .isInstanceOf(EnumDeserializationException.class);
    }

    @Test
    void givenDurationStringInWrongFormat_whenMappingTimeRequestToTimeDto_thenDurationDeserializationExceptionIsThrown() {
      MkApiTimeRequest request = validTimeRequest().duration("1234515");

      assertThatThrownBy(() -> mapper.map(request))
          .isInstanceOf(DurationDeserializationException.class);
    }

    private static MkApiTimeRequest validTimeRequest() {
      MkApiCombination combination = new MkApiCombination()
          .tires(COMBINATION.getTires().name())
          .vehicle(COMBINATION.getVehicle().name())
          .driver(COMBINATION.getDriver().name())
          .glider(COMBINATION.getGlider().name());

      return new MkApiTimeRequest()
          .track(TRACK.getName())
          .username(USERNAME)
          .duration(CustomDurationSerializer.serializeDuration(DURATION))
          .combination(combination);
    }
  }

  @Test
  void givenValidTimeDto_whenMappingTimeDtoToTime_thenMappingSetsFieldCreatedAt() {
    TimeDto timeDto = validTimeDto();
    timeDto.setCreatedAt(null);

    Time result = mapper.map(timeDto, TIMESTAMP);

    assertThat(result)
        .extracting(
            Time::getUsername,
            Time::getTrack,
            Time::getDuration,
            Time::getCreatedAt,
            Time::getCombination)
        .containsExactly(
            USERNAME,
            TRACK,
            DURATION,
            TIMESTAMP,
            COMBINATION);
  }

  @Test
  void givenValidTime_whenMappingTimeToTimeDto_thenMappingWorksCorrectly() {
    Time time = validTime();

    TimeDto result = mapper.map(time);

    assertThat(result)
        .extracting(
            TimeDto::getUsername,
            TimeDto::getTrack,
            TimeDto::getDuration,
            TimeDto::getCreatedAt,
            TimeDto::getCombination)
        .containsExactly(
            USERNAME,
            TRACK,
            DURATION,
            TIMESTAMP,
            COMBINATION);
  }

  @Test
  void givenValidTimeDto_whenMappingTimeDtoToTimeResponse_thenMappingWorksCorrectly() {
    TimeDto timeDto = validTimeDto();
    MkApiCombination mappedCombination = new MkApiCombination()
        .driver(COMBINATION.getDriver().getName())
        .vehicle(COMBINATION.getVehicle().getValue())
        .tires(COMBINATION.getTires().getValue())
        .glider(COMBINATION.getGlider().getValue());
    when(combinationMapper.map(any(Combination.class)))
        .thenReturn(mappedCombination);

    MkApiTimeResponse result = mapper.map(timeDto);

    assertThat(result)
        .extracting(
            MkApiTimeResponse::getUsername,
            MkApiTimeResponse::getTrack,
            MkApiTimeResponse::getDuration,
            MkApiTimeResponse::getCreatedAt,
            MkApiTimeResponse::getCombination)
        .containsExactly(
            USERNAME,
            TRACK.getName(),
            CustomDurationSerializer.serializeDuration(DURATION),
            TIMESTAMP,
            mappedCombination);
  }

  private static TimeDto validTimeDto() {
    return TimeDto.builder()
        .username(USERNAME)
        .track(TRACK)
        .duration(DURATION)
        .createdAt(TIMESTAMP)
        .combination(COMBINATION)
        .build();
  }

  private static Time validTime() {
    return Time.builder()
        .id("id")
        .username(USERNAME)
        .track(TRACK)
        .duration(DURATION)
        .createdAt(TIMESTAMP)
        .combination(COMBINATION)
        .build();
  }
}