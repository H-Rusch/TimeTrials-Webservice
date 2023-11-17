package com.hrusch.webapp.repository;

import com.hrusch.webapp.common.UserDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class UserEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "user_id", nullable = false, unique = true)
    private String userId;

    @Column(name = "username", length = 24, nullable = false, unique = true)
    private String username;

    @Column(name = "encrypted_password", nullable = false)
    private String encryptedPassword;

    @OneToMany(mappedBy = "user")
    private final Set<TimeEntity> times = new HashSet<>();

    public static UserEntity from(UserDto userDto) {
        return UserEntity.builder()
                .userId(userDto.getUserId())
                .username(userDto.getUsername())
                .encryptedPassword(userDto.getEncryptedPassword())
                .build();
    }
}
