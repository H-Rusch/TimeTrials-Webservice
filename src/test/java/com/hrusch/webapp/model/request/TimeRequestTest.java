package com.hrusch.webapp.model.request;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hrusch.webapp.model.Track;
import com.hrusch.webapp.model.request.TimeRequest;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Duration;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TimeRequestTest {

    @Autowired
    private static Validator validator;

    private ObjectMapper objectMapper;
    private static final Duration validDuration = Duration.parse("PT1M23.456S");
    private static final String validUserId = "abcd-efgh-ijkl-mnop";

    @BeforeAll
    public static void setUpValidator() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    void deserializingTimeRequest_whenGivenValidJson_produceCorrectObject() throws Exception {
        String request = "{\"userId\": \"abcd-efgh-ijkl-mnop\", \"time\": \"1:23.456\", \"track\": \"WATER_PARK\"}";

        TimeRequest timeRequest = objectMapper.readValue(request, TimeRequest.class);

        assertThat(timeRequest.getUserId()).isEqualTo(validUserId);
        assertThat(timeRequest.getTrack()).isEqualTo(Track.WATER_PARK);
        assertThat(timeRequest.getTime()).isEqualTo(validDuration);
    }

    @ParameterizedTest
    @MethodSource("exceptionCausingRequests")
    void deserializingTimeRequest_whenGivenInvalidJson_throwJsonException(String requestJson) {
        assertThrows(JsonProcessingException.class, () -> objectMapper.readValue(requestJson, TimeRequest.class));
    }

    public static Stream<Arguments> exceptionCausingRequests() {
        return Stream.of(
                Arguments.of("{\"userId\": \"abcd-efgh-ijkl-mnop\", \"time\": \"23.456\", \"track\": \"WATER_PARK\"}"),
                Arguments.of("{\"userId\": \"abcd-efgh-ijkl-mnop\", \"time\": \"\", \"track\": \"WATER_PARK\"}"),
                Arguments.of("{\"userId\": \"abcd-efgh-ijkl-mnop\", \"time\": \"1:23.456\", \"track\": \"INVALID_TRACK\"}")
        );
    }

    @Test
    void validateTimeRequest_whenGivenValidTimeRequest_produceNoValidationErrors() {
        var timeRequest = new TimeRequest();
        timeRequest.setUserId(validUserId);
        timeRequest.setTime(validDuration);
        timeRequest.setTrack(Track.BABY_PARK_GCN);

        var validationErrors = validator.validate(timeRequest);

        assertThat(validationErrors).isEmpty();
    }

    @Test
    void validateTimeRequest_whenGivenInvalidTimeRequest_produceValidationErrors() {
        var timeRequest = new TimeRequest();

        var validationErrors = validator.validate(timeRequest);

        assertThat(validationErrors).hasSize(3);
    }
}
