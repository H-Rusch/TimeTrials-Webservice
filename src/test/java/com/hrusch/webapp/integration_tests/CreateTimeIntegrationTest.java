package com.hrusch.webapp.integration_tests;

import com.hrusch.webapp.TimeUtil;
import com.hrusch.webapp.UserUtil;
import com.hrusch.webapp.model.dto.TimeDto;
import com.hrusch.webapp.model.request.TimeRequest;
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

import java.util.Objects;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CreateTimeIntegrationTest {

    @LocalServerPort
    private int port;
    private String baseUrl;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TimeRepository timeRepository;

    private final UUID uuid = UUID.randomUUID();
    private TimeRequest timeRequest;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/times";

        var userEntity = UserUtil.createEntity(uuid);
        userRepository.save(userEntity);

        timeRequest = TimeUtil.createTimeRequestModel();
        timeRequest.setUserId(uuid.toString());
    }

    @AfterEach
    void cleanUpTimeDatabase() {
        timeRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void createTime_whenGivenValidTimeRequest_shouldReturn201Response() {

        ResponseEntity<TimeDto> response = restTemplate.postForEntity(baseUrl, timeRequest, TimeDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(Objects.requireNonNull(response.getBody()).getUsername()).isNotNull();
        assertThat(timeRepository.findAll()).hasSize(1);
    }

    @Test
    void createTime_whenGivenTimeRequestForNonExistingUser_shouldReturn200Response() {
        TimeRequest timeRequest = TimeUtil.createTimeRequestModel();

        ResponseEntity<TimeDto> response = restTemplate.postForEntity(baseUrl, timeRequest, TimeDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(timeRepository.findAll()).isEmpty();
    }
}
