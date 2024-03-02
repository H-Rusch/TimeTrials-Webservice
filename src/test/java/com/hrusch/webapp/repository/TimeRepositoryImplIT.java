package com.hrusch.webapp.repository;


import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hrusch.webapp.model.Time;
import com.hrusch.webapp.model.Track;
import com.hrusch.webapp.util.MongoDBTestContainerConfig;
import com.hrusch.webapp.util.TestDataReader;
import java.time.Duration;
import java.util.Optional;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@DataMongoTest
@ExtendWith(SpringExtension.class)
class TimeRepositoryImplIT extends MongoDBTestContainerConfig {

  private static final String COLLECTION = "times";
  private static final String DATA_DIRECTORY = "repository";

  @Autowired
  private MongoTemplate mongoTemplate;

  private TimeRepository subject;

  @BeforeEach
  void fillDatabase() {
    subject = new TimeRepositoryImpl(mongoTemplate);

    TestDataReader.readAllFilesInDirectory(DATA_DIRECTORY)
        .stream()
        .map(TimeRepositoryImplIT::convertJsonToTime)
        .forEach(time -> mongoTemplate.insert(time, COLLECTION));
  }

  @SneakyThrows
  private static Time convertJsonToTime(String json) {
    return new ObjectMapper().readValue(json, Time.class);
  }

  @AfterEach
  void clearDatabase() {
    mongoTemplate.remove(new Query(), COLLECTION);
  }


  @Nested
  class TimeRepositoryImplFindBestTimeForTrackTest {

    @Test
    void givenNoParameter_whenFindBestTime_thenReturnEmptyOptional() {
      // given, when & then
      assertThat(subject.findBestTimeForTrack(null, null))
          .isEmpty();
    }

    @Test
    void givenOnlyUsername_whenFindBestTime_thenReturnEmptyOptional() {
      // given, when & then
      assertThat(subject.findBestTimeForTrack(null, "name1"))
          .isEmpty();
    }

    @Test
    void givenOnlyTrack_whenFindBestTime_thenReturnBestTimeForTrack() {
      // given
      Track track = Track.BABY_PARK_GCN;
      String expectedUsername = "name2";
      Duration expectedDuration = Duration.parse("PT1M1.48S");

      // given
      Optional<Time> result = subject.findBestTimeForTrack(track, null);

      // then
      assertThat(result).isPresent();
      assertThat(result.get())
          .extracting(
              Time::getUsername,
              Time::getTrack,
              Time::getDuration)
          .containsExactly(
              expectedUsername,
              track,
              expectedDuration);
    }

    @Test
    void givenTrackAndUsername_whenFindBestTime_thenReturnBestTimeForUserOnTrack() {
      // given
      String username = "name1";
      Track track = Track.BABY_PARK_GCN;
      Duration expectedDuration = Duration.parse("PT1M7.48S");

      // when
      Optional<Time> result = subject.findBestTimeForTrack(track, username);

      // then
      assertThat(result).isPresent();
      assertThat(result.get())
          .extracting(
              Time::getUsername,
              Time::getTrack,
              Time::getDuration)
          .containsExactly(
              username,
              track,
              expectedDuration);
    }
  }
}