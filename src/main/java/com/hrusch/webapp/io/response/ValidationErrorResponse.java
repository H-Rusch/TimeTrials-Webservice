package com.hrusch.webapp.io.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ValidationErrorResponse {

    private List<ValidationError> violations = new ArrayList<>();
}
