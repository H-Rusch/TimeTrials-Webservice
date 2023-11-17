package com.hrusch.webapp.repository;

import com.hrusch.webapp.TimeUtil;
import com.hrusch.webapp.UserUtil;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class TimeEntityTest {

    private static final UUID uuid = UUID.randomUUID();

    @Test
    void convertingFromTimeDto_producesCorrectObject() {
        var userEntity = UserUtil.createEntity(uuid);
        var timeDto = TimeUtil.createTimeDto(uuid);

        var timeEntity = TimeEntity.from(timeDto, userEntity);

        assertThat(timeEntity.getTime()).isEqualTo(timeDto.getTime());
        assertThat(timeEntity.getTrack()).isEqualTo(timeDto.getTrack());
        assertThat(timeEntity.getUser()).isEqualTo(userEntity);
        assertThat(timeEntity.getCreatedAt()).isEqualTo(timeDto.getCreatedAt());
    }
}
