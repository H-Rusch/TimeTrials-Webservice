package com.hrusch.webapp.io.controller;

import com.hrusch.webapp.common.UserDto;
import com.hrusch.webapp.exception.UsernameAlreadyTakenException;
import com.hrusch.webapp.io.request.UserRequest;
import com.hrusch.webapp.io.response.UserResponse;
import com.hrusch.webapp.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public UserResponse createUser(@RequestBody @Valid UserRequest userRequest) throws UsernameAlreadyTakenException {
        UserDto userToCreate = UserDto.from(userRequest);

        UserDto createdUser = userService.createUser(userToCreate);

        return createUserResponse(createdUser);
    }

    @ExceptionHandler(UsernameAlreadyTakenException.class)
    public ResponseEntity<Object> handleException(UsernameAlreadyTakenException e) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(e.getMessage());
    }



    public UserResponse createUserResponse(UserDto userDto) {
        return UserResponse.builder()
                .userId(userDto.getUserId())
                .username(userDto.getUsername())
                .build();
    }
}
