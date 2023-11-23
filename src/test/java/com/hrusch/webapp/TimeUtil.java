package com.hrusch.webapp;

import com.hrusch.webapp.model.Track;
import com.hrusch.webapp.model.dto.TimeDto;
import com.hrusch.webapp.model.entity.TimeEntity;
import com.hrusch.webapp.model.entity.UserEntity;
import com.hrusch.webapp.model.request.TimeRequest;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

public class TimeUtil {

    private static final ModelMapper modelMapper = new ModelMapper();

    static {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    }


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
        TimeDto timeDto = modelMapper.map(requestModel, TimeDto.class);
        timeDto.setCreatedAt(LocalDateTime.now());

        return timeDto;
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
