package com.hrusch.timetrials.webservice.repository;


import static com.hrusch.timetrials.webservice.model.Track.BABY_PARK_GCN;
import static com.hrusch.timetrials.webservice.model.Track.MARIO_CIRCUIT;
import static org.assertj.core.api.Assertions.assertThat;

import com.hrusch.timetrials.webservice.config.JacksonConfig;
import com.hrusch.timetrials.webservice.config.MongoConfig;
import com.hrusch.timetrials.webservice.model.Time;
import com.hrusch.timetrials.webservice.model.Track;
import com.hrusch.timetrials.webservice.testutils.MongoDBTestcontainersConfig;
import com.hrusch.timetrials.webservice.testutils.TestDataReader;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import lombok.SneakyThrows;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@DataMongoTest
@ExtendWith(SpringExtension.class)
@Import({MongoConfig.class, MongoDBTestcontainersConfig.class})
class TimeRepositoryImplIT {

  private static final String COLLECTION = "times";
  private static final String DATA_DIRECTORY = "repository";

  private final TestDataReader testDataReader = new TestDataReader("testdata", DATA_DIRECTORY);

  @Autowired
  private MongoTemplate mongoTemplate;

  private TimeRepository subject;

  @BeforeEach
  void fillDatabase() {
    subject = new TimeRepositoryImpl(mongoTemplate);

    testDataReader.readAllFilesInDirectory()
        .stream()
        .map(TimeRepositoryImplIT::convertJsonToTime)
        .forEach(time -> mongoTemplate.insert(time, COLLECTION));
  }

  @SneakyThrows
  private static Time convertJsonToTime(String json) {
    return new JacksonConfig().objectMapper().readValue(json, Time.class);
  }

  @AfterEach
  void clearDatabase() {
    mongoTemplate.remove(new Query(), COLLECTION);
  }


  @Nested
  class TimeRepositoryImplFindBestTimeForTrackTest {

    @Test
    void givenNoParameter_whenFindBestTime_thenReturnEmptyList() {
      // given, when & then
      assertThat(subject.findBestTimeForTrack(null, null))
          .isEmpty();
    }

    @Test
    void givenOnlyUsername_whenFindBestTime_thenReturnEmptyList() {
      // given, when & then
      assertThat(subject.findBestTimeForTrack(null, "name1"))
          .isEmpty();
    }

    @Test
    void givenOnlyTrack_whenFindBestTime_thenReturnBestTimesForTrack() {
      // given
      Track track = BABY_PARK_GCN;
      String expectedUsername = "name2";
      Duration expectedDuration = Duration.parse("PT1M1.48S");

      // given
      Collection<Time> result = subject.findBestTimeForTrack(track, null);

      // then
      assertThat(result)
          .hasSize(1)
          .first()
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
    void givenTrackForTrackHavingWrTie_whenFindBestTime_thenReturnBestTimesForTrack() {
      // given
      Track track = MARIO_CIRCUIT;
      List<String> expectedUsernames = List.of("name1", "name3");
      Duration expectedDuration = Duration.parse("PT1M34.123S");

      // given
      Collection<Time> result = subject.findBestTimeForTrack(track, null);

      // then
      assertThat(result)
          .hasSize(2)
          .allMatch(
              time -> time.getTrack().equals(track) && time.getDuration().equals(expectedDuration))
          .extracting(Time::getUsername)
          .containsExactlyInAnyOrderElementsOf(expectedUsernames);
    }

    @Test
    void givenTrackAndUsername_whenFindBestTime_thenReturnBestTimeForUserOnTrackEvenIfThereWasATrie() {
      // given
      String username = "name1";
      Track track = MARIO_CIRCUIT;
      Duration expectedDuration = Duration.parse("PT1M34.123S");

      // when
      Collection<Time> result = subject.findBestTimeForTrack(track, username);

      // then
      assertThat(result)
          .hasSize(1)
          .first()
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

  @Nested
  class TimeRepositoryImplFindBestTimeForEachTrackTest {

    @Test
    void givenNoParameters_whenFindBestTimeForEachTrack_thenReturnedListContainsOnlyTheBestTimes() {
      // given & when
      Collection<Collection<Time>> result = subject.findBestTimeForEachTrack(null);
      System.out.println(result);

      // then
      assertThat(result)
          .hasSize(2);
      List<Time> flattenedRecords = result.stream()
          .flatMap(Collection::stream)
          .toList();
      assertThat(flattenedRecords)
          .hasSize(3)
          .extracting(
              Time::getUsername,
              Time::getTrack,
              Time::getDuration)
          .containsExactlyInAnyOrder(
              Tuple.tuple("name2", BABY_PARK_GCN, Duration.parse("PT1M1.48S")),
              Tuple.tuple("name1", MARIO_CIRCUIT, Duration.parse("PT1M34.123S")),
              Tuple.tuple("name3", MARIO_CIRCUIT, Duration.parse("PT1M34.123S")));
    }

    @Test
    void givenUsername_whenFindBestTimeForEachTrack_thenReturnedListContainsOnlyTheBestTimesForThatUser() {
      // given
      String username = "name1";

      // when
      Collection<Collection<Time>> result = subject.findBestTimeForEachTrack(username);

      // then
      assertThat(result)
          .hasSize(2)
          .allMatch(times -> times.size() == 1);
      List<Time> flattenedTimes = result.stream()
          .flatMap(Collection::stream)
          .toList();
      assertThat(flattenedTimes)
          .extracting(
              Time::getUsername,
              Time::getTrack,
              Time::getDuration)
          .containsExactlyInAnyOrder(
              Tuple.tuple(username, BABY_PARK_GCN, Duration.parse("PT1M7.48S")),
              Tuple.tuple(username, Track.MARIO_CIRCUIT, Duration.parse("PT1M34.123S")));
    }
  }

  @Nested
  class TimeRepositorySaveTimeTest {

    @Test
    void givenTime_whenSaveTime_thenTimeInDatabase() {
      // given
      Time time = createSampleTime();

      // when
      Time savedTime = subject.saveTime(time);

      // then
      assertThat(mongoTemplate.findById(savedTime.getId(), Time.class, COLLECTION))
          .isNotNull();
    }

    @Test
    void givenTime_whenSaveTime_thenReturnedObjectHasSameValuesAndFilledId() {
      // given
      Time time = createSampleTime();

      // when
      Time savedTime = subject.saveTime(time);

      // then
      assertThat(savedTime.getId())
          .isNotNull();
      assertThat(savedTime)
          .extracting(
              Time::getUsername,
              Time::getTrack,
              Time::getDuration,
              Time::getCreatedAt)
          .containsExactly(
              savedTime.getUsername(),
              savedTime.getTrack(),
              savedTime.getDuration(),
              savedTime.getCreatedAt());
    }

    private static Time createSampleTime() {
      return Time.builder()
          .username("username")
          .duration(Duration.parse("PT1M7.48S"))
          .track(BABY_PARK_GCN)
          .createdAt(LocalDateTime.now())
          .build();
    }
  }
}