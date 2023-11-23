package com.hrusch.webapp.integration_tests;

import com.hrusch.webapp.UserUtil;
import com.hrusch.webapp.model.dto.UserDto;
import com.hrusch.webapp.model.errorResponse.ApiError;
import com.hrusch.webapp.model.request.UserRequest;
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
import org.springframework.test.context.jdbc.Sql;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CreateUserIntegrationTest {

    @LocalServerPort
    private int port;

    private String baseUrl;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        baseUrl = "http://localhost:" + port + "/users";
    }

    @AfterEach
    void emptyDatabase() {
        userRepository.deleteAll();
    }

    @Test
    void creatingUser_whenSendingValidUserRequest_return201Response() {
        UserRequest userRequest = UserUtil.createUserRequestModel();

        ResponseEntity<UserDto> response = restTemplate.postForEntity(baseUrl, userRequest, UserDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(Objects.requireNonNull(response.getBody()).getUserId()).isNotNull();
        assertThat(userRepository.findAll()).hasSize(1);
    }

    @Test
    void creatingUser_whenSendingInvalidUserRequest_return400Response() {
        UserRequest userRequest = UserUtil.createUserRequestModel();
        userRequest.setPassword("000");

        ResponseEntity<UserDto> response = restTemplate.postForEntity(baseUrl, userRequest, UserDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(userRepository.findAll()).isEmpty();
    }

    @Test
    @Sql(statements = "INSERT INTO users (user_id, username, encrypted_password) VALUES ('user_id', 'Testing', 'encrypted')", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void creatingUser_whenSendingUserRequestWithNonUniqueName_return409Response() {
        assertThat(userRepository.findAll()).hasSize(1);
        UserRequest userRequest = UserUtil.createUserRequestModel();

        ResponseEntity<ApiError> response = restTemplate.postForEntity(baseUrl, userRequest, ApiError.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }
}
