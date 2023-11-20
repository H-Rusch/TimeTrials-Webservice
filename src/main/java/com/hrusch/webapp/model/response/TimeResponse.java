package com.hrusch.webapp.model.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.hrusch.webapp.model.TimeDto;
import com.hrusch.webapp.model.Track;
import com.hrusch.webapp.model.serialization.CustomDurationDeserializer;
import com.hrusch.webapp.model.serialization.CustomDurationSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TimeResponse {

    private String username;

    private Track track;

    @JsonSerialize(using = CustomDurationSerializer.class)
    @JsonDeserialize(using = CustomDurationDeserializer.class)
    private Duration time;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss")
    private LocalDateTime createdAt;

    public static TimeResponse from(TimeDto timeDto) {
        return TimeResponse.builder()
                .username(timeDto.getUsername())
                .track(timeDto.getTrack())
                .time(timeDto.getTime())
                .createdAt(timeDto.getCreatedAt())
                .build();
    }
}
