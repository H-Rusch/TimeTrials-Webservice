package com.hrusch.webapp.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.hrusch.webapp.model.TimeEntity;
import com.hrusch.webapp.model.Track;
import com.hrusch.webapp.model.request.TimeRequest;
import com.hrusch.webapp.model.serialization.CustomDurationDeserializer;
import com.hrusch.webapp.model.serialization.CustomDurationSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.time.Duration;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TimeDto {

    @Id
    private Long id;

    @JsonIgnore
    private String userId;

    private String username;

    private Track track;

    @JsonSerialize(using = CustomDurationSerializer.class)
    @JsonDeserialize(using = CustomDurationDeserializer.class)
    private Duration time;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss")
    private LocalDateTime createdAt;
}
