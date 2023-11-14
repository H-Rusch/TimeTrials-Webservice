package com.hrusch.webapp.io.controller;

import com.hrusch.webapp.common.UserDto;
import com.hrusch.webapp.io.request.UserRequest;
import com.hrusch.webapp.io.response.UserResponse;
import com.hrusch.webapp.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController()
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * POST endpoint for creating a new user.
     *
     * @param userRequest the user to create
     * @return a representation of the created user
     */
    @PostMapping()
    public UserResponse createUser(@RequestBody @Valid UserRequest userRequest) {
        UserDto userToCreate = createUserDto(userRequest);

        UserDto createdUser = userService.createUser(userToCreate);

        return createUserRest(createdUser);
    }

    public UserDto createUserDto(UserRequest userRequest) {
        return UserDto.builder()
                .userId(UUID.randomUUID().toString())
                .username(userRequest.getUsername())
                .password(userRequest.getPassword())
                .encryptedPassword("")
                .build();
    }

    public UserResponse createUserRest(UserDto userDto) {
        return UserResponse.builder()
                .userId(userDto.getUserId())
                .username(userDto.getUsername())
                .build();
    }
}
