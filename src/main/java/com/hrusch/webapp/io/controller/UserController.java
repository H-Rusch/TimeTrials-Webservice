package com.hrusch.webapp.io.controller;

import com.hrusch.webapp.common.UserDto;
import com.hrusch.webapp.io.request.UserRequest;
import com.hrusch.webapp.io.response.UserRest;
import com.hrusch.webapp.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController()
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping()
    public UserRest createUser(@RequestBody @Valid UserRequest userRequest) {
        UserDto userToCreate = createUserDto(userRequest);

        UserDto createdUser = userService.createUser(userToCreate);

        return createUserRest(createdUser);
    }

    public UserDto createUserDto(UserRequest userRequest) {
        UserDto userDto = new UserDto();
        userDto.setUserId(UUID.randomUUID().toString());
        userDto.setUsername(userRequest.getUsername());
        userDto.setPassword(userRequest.getPassword());
        userDto.setEncryptedPassword("");

        return userDto;
    }

    public UserRest createUserRest(UserDto userDto) {
        UserRest userRest = new UserRest();
        userRest.setUserId(userDto.getUserId());
        userRest.setUsername(userDto.getUsername());

        return userRest;
    }
}
