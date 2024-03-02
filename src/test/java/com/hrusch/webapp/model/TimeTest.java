package com.hrusch.webapp.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hrusch.webapp.config.JacksonConfig;
import com.hrusch.webapp.model.combination.Combination;
import com.hrusch.webapp.model.combination.Driver;
import com.hrusch.webapp.model.combination.Glider;
import com.hrusch.webapp.model.combination.Tires;
import com.hrusch.webapp.model.combination.Vehicle;
import com.hrusch.webapp.util.FileReader;
import java.time.Duration;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class TimeTest {

  private static final String DIRECTORY = "model";

  private final FileReader fileReader = new FileReader();
  private final ObjectMapper objectMapper = new JacksonConfig().objectMapper();

  private final LocalDateTime timestamp = LocalDateTime.of(2023, 5, 25, 13, 37, 42);
  private final Duration duration = Duration.parse("PT1M7.48S");

  private Time subject;

  @BeforeEach
  void setUp() {
    subject =
        Time.builder()
            .username("name")
            .duration(duration)
            .track(Track.BABY_PARK_GCN)
            .createdAt(timestamp)
            .combination(
                Combination.builder()
                    .driver(Driver.FUNKY_KONG)
                    .vehicle(Vehicle.BADWAGON)
                    .tires(Tires.ROLLER)
                    .glider(Glider.FLOWER_GLIDER)
                    .build())
            .build();
  }

  @Test
  void givenTimeObjectWithCombination_whenSerializing_createJsonWithCombination()
      throws JsonProcessingException {
    // given
    String expected = fileReader.readFileToString(DIRECTORY, "time_valid_including_combination.json");

    // when
    String json = objectMapper.writeValueAsString(subject);

    // then
    assertThat(json).isEqualTo(expected);
  }

  @Test
  void givenTimeObjectWithoutCombination_whenSerializing_createJsonWithoutCombination()
          throws JsonProcessingException {
    // given
    String expected = fileReader.readFileToString(DIRECTORY, "time_valid_missing_combination.json");
    subject.setCombination(null);

    // when
    String json = objectMapper.writeValueAsString(subject);

    // then
    assertThat(json).isEqualTo(expected);
  }

  @Test
  void givenValidTimeJson_whenDeserializing_thenCorrectObjectCreated()
      throws JsonProcessingException {
    // given
    String json = fileReader.readFileToString(DIRECTORY, "time_valid_missing_combination.json");

    // when
    Time time = objectMapper.readValue(json, Time.class);

    // then
    // TODO refactor with extracting
    assertThat(time.getUsername()).isEqualTo("name");
    assertThat(time.getTrack()).isEqualTo(Track.BABY_PARK_GCN);
    assertThat(time.getDuration()).isEqualTo(duration);
    assertThat(time.getCreatedAt()).isEqualTo(timestamp);
  }

  @ParameterizedTest
  @ValueSource(
      strings = {"time_invalid_default_duration.json", "time_invalid_default_timestamp.json"})
  void givenInvalidTimeJson_whenDeserializing_thenExceptionIsThrown(String filename) {
    // given
    String json = fileReader.readFileToString(DIRECTORY, filename);

    // when & then
    assertThatThrownBy(() -> objectMapper.readValue(json, Time.class))
        .isInstanceOf(JsonProcessingException.class);
  }
}
