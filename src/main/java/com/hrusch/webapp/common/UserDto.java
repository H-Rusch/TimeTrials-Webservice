package com.hrusch.webapp.common;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserDto {

    private Long id;

    private String userId;

    private String username;

    private String password;

    private String encryptedPassword;

}
