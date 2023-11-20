package com.hrusch.webapp.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hrusch.webapp.model.request.UserRequest;
import com.hrusch.webapp.model.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    //@JsonIgnore
    @Id
    private Long id;
    private String userId;
    private String username;

    @JsonIgnore
    private String password;
    @JsonIgnore
    private String encryptedPassword;

    public static UserDto from(UserRequest userRequest) {
        return UserDto.builder()
                .userId(UUID.randomUUID().toString())
                .username(userRequest.getUsername())
                .password(userRequest.getPassword())
                .build();
    }

    public static UserDto from(UserEntity entity) {
        return UserDto.builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .username(entity.getUsername())
                .encryptedPassword(entity.getEncryptedPassword())
                .build();
    }
}
