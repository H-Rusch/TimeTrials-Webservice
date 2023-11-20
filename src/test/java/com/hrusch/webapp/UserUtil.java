package com.hrusch.webapp;

import com.hrusch.webapp.model.UserEntity;
import com.hrusch.webapp.model.dto.UserDto;
import com.hrusch.webapp.model.request.UserRequest;
import org.modelmapper.ModelMapper;

import java.util.UUID;

public class UserUtil {

    private static final ModelMapper modelMapper = new ModelMapper();

    public static UserRequest createUserRequestModel() {
        UserRequest requestModel = new UserRequest();
        requestModel.setUsername("Testing");
        requestModel.setPassword("password123");
        requestModel.setRepeatedPassword("password123");

        return requestModel;
    }

    public static UserDto createUserDtoFromRequestModel(UserRequest requestModel) {
        UserDto userDto = modelMapper.map(requestModel, UserDto.class);
        userDto.setId(1L);
        userDto.setUserId(UUID.randomUUID().toString());

        return userDto;
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
