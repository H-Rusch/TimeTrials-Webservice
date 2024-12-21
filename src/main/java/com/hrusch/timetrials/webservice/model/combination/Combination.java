package com.hrusch.timetrials.webservice.model.combination;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class  Combination {

  @NotNull
  private Driver driver;
  @NotNull
  private Vehicle vehicle;
  @NotNull
  private Tires tires;
  @NotNull
  private Glider glider;
}