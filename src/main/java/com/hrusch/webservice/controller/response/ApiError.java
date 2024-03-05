package com.hrusch.webservice.controller.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@NoArgsConstructor
public class ApiError {

  private HttpStatus status;
  private String message;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime timestamp;
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private List<ApiSubError> subErrors;

  public ApiError(HttpStatus status, String message) {
    this.status = status;
    this.message = message;
    this.timestamp = LocalDateTime.now();
  }

  public void addSubError(ApiSubError subError) {
    if (subErrors == null) {
      subErrors = new ArrayList<>();
    }
    subErrors.add(subError);
  }
}
