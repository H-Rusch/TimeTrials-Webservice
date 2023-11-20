package com.hrusch.webapp.model;

import com.hrusch.webapp.UserUtil;
import com.hrusch.webapp.model.UserEntity;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class UserEntityTest {

    @Test
    void from_whenGivenUserDto_convertToCorrectUserEntity() {
        var userDto = UserUtil.createDto(UUID.randomUUID());

        var userEntity = UserEntity.from(userDto);

        assertThat(userEntity.getId()).isNull();
        assertThat(userEntity.getUserId()).isEqualTo(userDto.getUserId());
        assertThat(userEntity.getUsername()).isEqualTo(userDto.getUsername());
        assertThat(userEntity.getEncryptedPassword()).isEqualTo(userDto.getEncryptedPassword());
        assertThat(userEntity.getTimes()).isEmpty();
    }
}
