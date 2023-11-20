package com.hrusch.webapp.io.response;

import com.hrusch.webapp.UserUtil;
import com.hrusch.webapp.model.response.UserResponse;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class UserResponseTest {

    @Test
    void from_whenGivenUserDto_convertToCorrectUserResponse() {
        var userDto = UserUtil.createDto(UUID.randomUUID());

        var userResponse = UserResponse.from(userDto);

        assertThat(userResponse.getUserId()).isEqualTo(userDto.getUserId());
        assertThat(userResponse.getUsername()).isEqualTo(userDto.getUsername());
    }
}
