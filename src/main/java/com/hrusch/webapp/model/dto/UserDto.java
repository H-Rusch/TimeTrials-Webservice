package com.hrusch.webapp.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    @JsonIgnore
    private Long id;
    private String userId;
    private String username;

    @JsonIgnore
    private String password;
    @JsonIgnore
    private String encryptedPassword;
}
