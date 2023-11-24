package com.hrusch.webapp.integration_tests;

import com.hrusch.webapp.TimeUtil;
import com.hrusch.webapp.UserUtil;
import com.hrusch.webapp.model.Track;
import com.hrusch.webapp.model.dto.TimeDto;
import com.hrusch.webapp.model.entity.UserEntity;
import com.hrusch.webapp.error.response.ApiError;
import com.hrusch.webapp.repository.TimeRepository;
import com.hrusch.webapp.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.Duration;
import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ListTimesIntegrationTest {

    @LocalServerPort
    private int port;
    private String baseUrl;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TimeRepository timeRepository;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/times";

        fillDatabases();
    }

    @AfterEach
    void cleanUpTimeDatabase() {
        timeRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void getBestTimes_whenRequestingAllBestTimes_returnCorrectValues() {
        var url = baseUrl + "/best";

        ResponseEntity<TimeDto[]> response = restTemplate.getForEntity(url, TimeDto[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(2);
        assertTrue(Arrays.stream(Objects.requireNonNull(response.getBody())).allMatch(time -> time.getUsername().equals("Username0")));
    }

    @Test
    void getBestTimes_whenRequestingAllBestTimesForUser_returnCorrectValues() {
        var username = "Username1";
        var url = baseUrl + "/best?username=" + username;

        ResponseEntity<TimeDto[]> response = restTemplate.getForEntity(url, TimeDto[].class);
        var times = Objects.requireNonNull(response.getBody());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(times).hasSize(2);
        assertTrue(Arrays.stream(times).allMatch(time ->
                time.getUsername().equals(username) && time.getTime().equals(Duration.parse("PT1M1S"))));
    }

    @Test
    void getBestTimes_whenRequestingAllBestTimesForUserWhoHasNoTimes_returnEmptyList() {
        var username = "Username2";
        createAndSaveUser(username);
        var url = baseUrl + "/best?username=" + username;

        ResponseEntity<TimeDto[]> response = restTemplate.getForEntity(url, TimeDto[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(response.getBody()).isNull();
    }

    @Test
    void getBestTimes_whenRequestingAllBestTimesForUserWhoDoesNotExist_returnError() {
        var username = "Username2";
        var url = baseUrl + "/best?username=" + username;

        ResponseEntity<ApiError> response = restTemplate.getForEntity(url, ApiError.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void getBestTimes_whenRequestingBestTimeOnTrack_returnCorrectTime() {
        var track = Track.BABY_PARK_GCN;
        var url = baseUrl + "/best/" + track.name();

        ResponseEntity<TimeDto> response = restTemplate.getForEntity(url, TimeDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTrack()).isEqualTo(track);
        assertThat(response.getBody().getUsername()).isEqualTo("Username0");
        assertThat(response.getBody().getTime()).isEqualTo(Duration.parse("PT1M"));
    }

    @Test
    void getBestTimes_whenRequestingBestTimeOnTrackForUser_returnCorrectTime() {
        var username = "Username1";
        var track = Track.BABY_PARK_GCN;
        var url = baseUrl + "/best/" + track.name() + "?username=" + username;

        ResponseEntity<TimeDto> response = restTemplate.getForEntity(url, TimeDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTrack()).isEqualTo(track);
        assertThat(response.getBody().getUsername()).isEqualTo(username);
        assertThat(response.getBody().getTime()).isEqualTo(Duration.parse("PT1M1S"));
    }

    @Test
    void getBestTimes_whenRequestingBestTimeOnTrackForUserWhoHasNoTimes_returnEmptyResponse() {
        var username = "Username1";
        var url = baseUrl + "/best/WATER_PARK?username=" + username;

        ResponseEntity<TimeDto> response = restTemplate.getForEntity(url, TimeDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(response.getBody()).isNull();
    }

    @Test
    void getBestTimes_whenRequestingBestTimeOnTrackForUserWhoDoesNotExist_returnError() {
        var username = "Username2";
        var url = baseUrl + "/best/BABY_PARK_GCN?username=" + username;

        ResponseEntity<ApiError> response = restTemplate.getForEntity(url, ApiError.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody()).isNotNull();
    }

    private void fillDatabases() {
        var duration = Duration.parse("PT1M");
        for (int i = 0; i < 2; i++) {
            createUserAndAddTimes("Username" + i, duration.plusSeconds(i));
        }
    }

    private void createUserAndAddTimes(String username, Duration time) {
        var userEntity = createAndSaveUser(username);

        Stream.of(Track.BABY_PARK_GCN, Track.BIG_BLUE)
                .map(track -> {
                    var timeEntity = TimeUtil.createTimeEntity(userEntity);
                    timeEntity.setId(null);
                    timeEntity.setTrack(track);
                    timeEntity.setTime(time);
                    return timeEntity;
                }).forEach(timeRepository::save);

        Stream.of(Track.BABY_PARK_GCN, Track.BIG_BLUE)
                .map(track -> {
                    var timeEntity = TimeUtil.createTimeEntity(userEntity);
                    timeEntity.setId(null);
                    timeEntity.setTrack(track);
                    timeEntity.setTime(time.plusSeconds(5));
                    return timeEntity;
                }).forEach(timeRepository::save);
    }

    private UserEntity createAndSaveUser(String username) {
        var userEntity = UserUtil.createEntity(UUID.randomUUID());
        userEntity.setId(null);
        userEntity.setUsername(username);
        return userRepository.save(userEntity);
    }
}

