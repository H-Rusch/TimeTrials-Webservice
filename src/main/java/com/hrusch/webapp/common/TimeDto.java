package com.hrusch.webapp.common;

import com.hrusch.webapp.io.request.TimeRequest;
import com.hrusch.webapp.repository.TimeEntity;
import lombok.Builder;
import lombok.Data;

import java.time.Duration;
import java.time.LocalDateTime;

@Data
@Builder
public class TimeDto {

    private Long id;
    private String userId;
    private String username;
    private Track track;
    private LocalDateTime createdAt;
    private Duration time;

    public static TimeDto from(TimeRequest request) {
        return TimeDto.builder()
                .userId(request.getUserId())
                .track(request.getTrack())
                .time(request.getTime())
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static TimeDto from(TimeEntity entity) {
        return TimeDto.builder()
                .id(entity.getId())
                .userId(entity.getUser().getUserId())
                .username(entity.getUser().getUsername())
                .track(entity.getTrack())
                .createdAt(entity.getCreatedAt())
                .time(entity.getTime())
                .build();
    }
}
