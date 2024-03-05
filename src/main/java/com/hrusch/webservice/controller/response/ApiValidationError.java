package com.hrusch.webservice.controller.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiValidationError implements ApiSubError {

  private String field;
  private Object rejectedValue;
  private String message;

  public ApiValidationError(String field, Object rejectedValue, String message) {
    this.field = field;
    this.rejectedValue = rejectedValue;
    this.message = message;
  }
}
