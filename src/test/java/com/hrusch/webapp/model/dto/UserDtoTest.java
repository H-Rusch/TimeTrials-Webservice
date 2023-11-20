package com.hrusch.webapp.model.dto;

import com.hrusch.webapp.TimeUtil;
import com.hrusch.webapp.UserUtil;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class UserDtoTest {

    @Test
    void from_whenGivenUserEntity_producedTimeDtoHasCorrectValues() {
        var user = UserUtil.createEntity(UUID.randomUUID());
        var timeEntity = TimeUtil.createTimeEntity(user);

        var timeDto = TimeDto.from(timeEntity);

        assertThat(timeDto.getId()).isNotNull();
        assertThat(timeDto.getCreatedAt()).isEqualTo(timeEntity.getCreatedAt());
        assertThat(timeDto.getUserId()).isEqualTo(timeEntity.getUser().getUserId());
        assertThat(timeDto.getUsername()).isEqualTo(timeEntity.getUser().getUsername());
        assertThat(timeDto.getTime()).isEqualTo(timeEntity.getTime());
        assertThat(timeDto.getTrack()).isEqualTo(timeEntity.getTrack());
    }
}
