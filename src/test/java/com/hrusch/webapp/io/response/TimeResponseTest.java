package com.hrusch.webapp.io.response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hrusch.webapp.TimeUtil;
import com.hrusch.webapp.common.Track;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class TimeResponseTest {

    @Test
    void serializationFormatIsCorrect() throws JsonProcessingException {
        var response = createSampleTimeResponse();
        ObjectMapper mapper = new ObjectMapper();

        var serializedObject = mapper.writeValueAsString(response);

        assertThat(serializedObject).isEqualTo("{\"username\":\"Testing123\",\"track\":\"[DS] Mario Circuit\",\"time\":\"1:07.480\",\"createdAt\":\"+1000000000-01-01@00:00:00\"}");
    }

    private TimeResponse createSampleTimeResponse() {
        return TimeResponse.builder()
                .username("Testing123")
                .track(Track.MARIO_CIRCUIT_DS)
                .createdAt(LocalDateTime.MIN)
                .time(TimeUtil.VALID_DURATION)
                .build();
    }
}
