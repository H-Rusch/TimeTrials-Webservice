package com.hrusch.webapp.common;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {

    private Long id;

    private String userId;

    private String username;

    private String password;

    private String encryptedPassword;

}
