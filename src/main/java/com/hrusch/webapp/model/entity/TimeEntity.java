package com.hrusch.webapp.model.entity;

import com.hrusch.webapp.model.Track;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDateTime;

@Entity
@Table(name = "times")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TimeEntity {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "track", nullable = false)
    private Track track;

    @Column(name = "time", nullable = false)
    private Duration time;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "user.id")
    private UserEntity user;
}
