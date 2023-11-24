package com.hrusch.webapp.controller;

import com.hrusch.webapp.error.exception.UsernameAlreadyTakenException;
import com.hrusch.webapp.model.dto.UserDto;
import com.hrusch.webapp.model.request.UserRequest;
import com.hrusch.webapp.service.UserService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController()
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    private final ModelMapper modelMapper;

    @Autowired
    public UserController(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    /**
     * POST endpoint for creating a new user.
     *
     * @param userRequest the user to create
     * @return a representation of the created user
     */
    @PostMapping()
    public ResponseEntity<UserDto> createUser(@RequestBody @Valid UserRequest userRequest) throws UsernameAlreadyTakenException {
        UserDto userToCreate = convertToDto(userRequest);

        UserDto createdUser = userService.createUser(userToCreate);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdUser);
    }

    UserDto convertToDto(UserRequest userRequest) {
        UserDto userDto = modelMapper.map(userRequest, UserDto.class);
        userDto.setUserId(UUID.randomUUID().toString());

        return userDto;
    }
}
