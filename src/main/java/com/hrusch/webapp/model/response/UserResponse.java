package com.hrusch.webapp.model.response;

import com.hrusch.webapp.model.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    private String userId;

    private String username;

    public static UserResponse from(UserDto userDto) {
        return UserResponse.builder()
                .userId(userDto.getUserId())
                .username(userDto.getUsername())
                .build();
    }
}
