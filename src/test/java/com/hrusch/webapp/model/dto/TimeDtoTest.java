package com.hrusch.webapp.model.dto;

import com.hrusch.webapp.TimeUtil;
import com.hrusch.webapp.UserUtil;
import com.hrusch.webapp.model.dto.TimeDto;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class TimeDtoTest {

    @Test
    void from_whenCreatingTimeDtoFromTimeRequest_producedTimeDtoHasCorrectValues() {
        var timeRequest = TimeUtil.createTimeRequestModel();

        var timeDto = TimeDto.from(timeRequest);

        assertThat(timeDto.getId()).isNull();
        assertThat(timeDto.getUsername()).isNull();
        assertThat(timeDto.getCreatedAt()).isNotNull();
        assertThat(timeDto.getUserId()).isEqualTo(timeRequest.getUserId());
        assertThat(timeDto.getTime()).isEqualTo(timeRequest.getTime());
        assertThat(timeDto.getTrack()).isEqualTo(timeRequest.getTrack());
    }

    @Test
    void from_whenCreatingTimeDtoFromTimeEntity_producedTimeDtoHasCorrectValues() {
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
