package com.hrusch.webapp.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.hrusch.webapp.model.combination.Combination;
import com.hrusch.webapp.model.serialization.CustomDurationDeserializer;
import java.time.Duration;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class TimeDto {

  private String username;

  private Track track;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private Combination combination;

  @JsonDeserialize(using = CustomDurationDeserializer.class)
  private Duration duration;

  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime createdAt;
}
