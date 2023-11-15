package com.hrusch.webapp.io.controller;

import com.hrusch.webapp.common.UserDto;
import com.hrusch.webapp.io.request.UserRequest;

import java.util.UUID;

class UserUtil {

    static UserRequest createUserRequestModel() {
        UserRequest requestModel = new UserRequest();
        requestModel.setUsername("Testing");
        requestModel.setPassword("password123");
        requestModel.setRepeatedPassword("password123");

        return requestModel;
    }

    static UserDto createUserDtoFromRequestModel(UserRequest requestModel) {
        return UserDto.builder()
                .id(1L)
                .userId(UUID.randomUUID().toString())
                .username(requestModel.getUsername())
                .password(requestModel.getPassword())
                .build();
    }
}
