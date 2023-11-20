package com.hrusch.webapp;

import com.hrusch.webapp.model.dto.UserDto;
import com.hrusch.webapp.model.request.UserRequest;
import com.hrusch.webapp.model.UserEntity;

import java.util.UUID;

public class UserUtil {

    public static UserRequest createUserRequestModel() {
        UserRequest requestModel = new UserRequest();
        requestModel.setUsername("Testing");
        requestModel.setPassword("password123");
        requestModel.setRepeatedPassword("password123");

        return requestModel;
    }

    public static UserDto createUserDtoFromRequestModel(UserRequest requestModel) {
        return UserDto.builder()
                .id(1L)
                .userId(UUID.randomUUID().toString())
                .username(requestModel.getUsername())
                .password(requestModel.getPassword())
                .build();
    }

    public static UserEntity createEntity(UUID uuid) {
        return UserEntity.builder()
                .id(1L)
                .userId(uuid.toString())
                .username("Test")
                .encryptedPassword("password123")
                .build();
    }

    public static UserDto createDto(UUID uuid) {
        return UserDto.builder()
                .id(1L)
                .userId(uuid.toString())
                .username("Test")
                .password("password123")
                .encryptedPassword("")
                .build();
    }
}
