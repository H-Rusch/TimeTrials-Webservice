package com.hrusch.webapp.repository;

import com.hrusch.webapp.common.TimeDto;
import com.hrusch.webapp.common.Track;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.time.Duration;
import java.time.LocalDateTime;

@Entity
@Table(name = "times")
@Data
@Builder
public class TimeEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "track", nullable = false)
    private Track track;

    @Column(name = "time", nullable = false)
    private Duration time;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "user.id")
    private UserEntity user;

    public static TimeEntity from(TimeDto timeDto, UserEntity user) {
        return TimeEntity.builder()
                .track(timeDto.getTrack())
                .time(timeDto.getTime())
                .createdAt(timeDto.getCreatedAt())
                .user(user)
                .build();
    }
}
