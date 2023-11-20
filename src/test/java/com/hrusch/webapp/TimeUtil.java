package com.hrusch.webapp;

import com.hrusch.webapp.model.TimeDto;
import com.hrusch.webapp.model.Track;
import com.hrusch.webapp.model.request.TimeRequest;
import com.hrusch.webapp.repository.TimeEntity;
import com.hrusch.webapp.repository.UserEntity;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

public class TimeUtil {

    public static final Duration VALID_DURATION = Duration.parse("PT1M7.48S");
    private static final LocalDateTime TIMESTAMP = LocalDateTime.now();

    public static TimeRequest createTimeRequestModel() {
        return TimeRequest.builder()
                .time(VALID_DURATION)
                .userId("abcdefgh-ijklmnop-qrstuvw")
                .track(Track.BABY_PARK_GCN)
                .build();
    }

    public static TimeDto createTimeDtoFromRequestModel(TimeRequest requestModel) {
        return TimeDto.from(requestModel);
    }

    public static TimeEntity createTimeEntity(UserEntity user) {
        return TimeEntity.builder()
                .id(1L)
                .user(user)
                .track(Track.BABY_PARK_GCN)
                .time(VALID_DURATION)
                .createdAt(TIMESTAMP)
                .build();
    }

    public static TimeDto createTimeDto(UUID uuid) {
        return TimeDto.builder()
                .id(1L)
                .userId(uuid.toString())
                .username("Test")
                .track(Track.BABY_PARK_GCN)
                .time(VALID_DURATION)
                .createdAt(TIMESTAMP)
                .build();
    }
}
