package com.hrusch.timetrials.webservice.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hrusch.timetrials.webservice.model.combination.Combination;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.Duration;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TimeDto {

  @NotNull
  @Size(min = 3, max = 32)
  private String username;

  private Track track;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private Combination combination;

  @NotNull
  private Duration duration;
}
