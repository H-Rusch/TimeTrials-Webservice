package com.hrusch.timetrials.webservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.hrusch.timetrials.webservice.model.combination.Combination;
import java.time.Duration;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document
public class Time {

  @Id
  @JsonIgnore
  private String id;

  private String username;

  private Track track;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private Combination combination;

  private Duration duration;

  private LocalDateTime createdAt;
}