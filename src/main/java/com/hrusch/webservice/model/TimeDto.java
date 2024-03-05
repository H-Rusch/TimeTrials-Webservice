package com.hrusch.webservice.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.hrusch.webservice.model.combination.Combination;
import com.hrusch.webservice.model.serialization.CustomDurationDeserializer;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.Duration;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class TimeDto {

  @NotNull
  @Size(min = 3, max = 32)
  private String username;

  private Track track;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private Combination combination;

  @NotNull
  @JsonDeserialize(using = CustomDurationDeserializer.class)
  private Duration duration;
}
