package com.hrusch.webapp.model.dto;

import com.hrusch.webapp.model.request.UserRequest;
import com.hrusch.webapp.model.UserEntity;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class UserDto {

    private Long id;

    private String userId;

    private String username;

    private String password;

    private String encryptedPassword;

    public static UserDto from(UserRequest userRequest) {
        return UserDto.builder()
                .userId(UUID.randomUUID().toString())
                .username(userRequest.getUsername())
                .password(userRequest.getPassword())
                .build();
    }

    public static UserDto from(UserEntity entity) {
        return UserDto.builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .username(entity.getUsername())
                .encryptedPassword(entity.getEncryptedPassword())
                .build();
    }
}
