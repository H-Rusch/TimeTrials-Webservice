package com.hrusch.timetrials.webservice.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hrusch.timetrials.webservice.config.JacksonConfig;
import com.hrusch.timetrials.webservice.model.combination.Combination;
import com.hrusch.timetrials.webservice.model.combination.Driver;
import com.hrusch.timetrials.webservice.model.combination.Glider;
import com.hrusch.timetrials.webservice.model.combination.Tires;
import com.hrusch.timetrials.webservice.model.combination.Vehicle;
import com.hrusch.timetrials.webservice.testutils.TestDataReader;
import java.time.Duration;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class TimeTest {

  private static final String DIRECTORY = "model";

  private final ObjectMapper objectMapper = new JacksonConfig().objectMapper();
  private final TestDataReader testDataReader = new TestDataReader("testdata", DIRECTORY);

  private final LocalDateTime timestamp = LocalDateTime.of(2023, 5, 25, 13, 37, 42);
  private final Duration duration = Duration.parse("PT1M7.48S");
  private final Combination combination = Combination.builder()
      .driver(Driver.FUNKY_KONG)
      .vehicle(Vehicle.BADWAGON)
      .tires(Tires.ROLLER)
      .glider(Glider.FLOWER_GLIDER)
      .build();

  private Time subject;

  @BeforeEach
  void setUp() {
    subject = Time.builder()
        .username("name")
        .duration(duration)
        .track(Track.BABY_PARK_GCN)
        .createdAt(timestamp)
        .combination(combination)
        .build();
  }

  @Test
  void givenTimeObjectWithCombination_whenSerializing_createJsonWithCombination()
      throws JsonProcessingException {
    // given
    String expected = testDataReader.readFileToString("time_valid_including_combination.json");

    // when
    String json = objectMapper.writeValueAsString(subject);

    // then
    assertThat(json).isEqualTo(expected);
  }

  @Test
  void givenTimeObjectWithoutCombination_whenSerializing_createJsonWithoutCombination()
      throws JsonProcessingException {
    // given
    String expected = testDataReader.readFileToString("time_valid_missing_combination.json");
    subject.setCombination(null);

    // when
    String json = objectMapper.writeValueAsString(subject);

    // then
    assertThat(json)
        .isEqualTo(expected);
  }

  @Test
  void givenValidTimeJsonWithoutCombination_whenDeserializing_thenCorrectObjectCreated()
      throws JsonProcessingException {
    // given
    String json = testDataReader.readFileToString("time_valid_missing_combination.json");

    // when
    Time time = objectMapper.readValue(json, Time.class);

    // then
    assertThat(time)
        .extracting(
            Time::getUsername,
            Time::getTrack,
            Time::getDuration,
            Time::getCreatedAt)
        .containsExactly(
            "name",
            Track.BABY_PARK_GCN,
            duration,
            timestamp);
  }

  @Test
  void givenValidTimeJsonWithCombination_whenDeserializing_thenCorrectObjectCreated()
      throws JsonProcessingException {
    // given
    String json = testDataReader.readFileToString("time_valid_including_combination.json");

    // when
    Time time = objectMapper.readValue(json, Time.class);

    // then
    assertThat(time)
        .extracting(
            Time::getUsername,
            Time::getTrack,
            Time::getDuration,
            Time::getCreatedAt,
            Time::getCombination)
        .containsExactly(
            "name",
            Track.BABY_PARK_GCN,
            duration,
            timestamp,
            combination);
  }

  @ParameterizedTest
  @ValueSource(
      strings = {"time_invalid_default_duration.json", "time_invalid_default_timestamp.json"})
  void givenInvalidTimeJson_whenDeserializing_thenExceptionIsThrown(String filename) {
    // given
    String json = testDataReader.readFileToString(filename);

    // when & then
    assertThatThrownBy(() -> objectMapper.readValue(json, Time.class))
        .isInstanceOf(JsonProcessingException.class);
  }
}
