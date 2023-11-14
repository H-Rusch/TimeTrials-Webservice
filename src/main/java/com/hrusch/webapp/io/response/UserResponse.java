package com.hrusch.webapp.io.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResponse {

    private String userId;

    private String username;
}
