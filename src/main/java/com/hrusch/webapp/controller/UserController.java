package com.hrusch.webapp.controller;

import com.hrusch.webapp.exception.UsernameAlreadyTakenException;
import com.hrusch.webapp.model.dto.UserDto;
import com.hrusch.webapp.model.request.UserRequest;
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
    public ResponseEntity<UserDto> createUser(@RequestBody @Valid UserRequest userRequest) throws UsernameAlreadyTakenException {
        UserDto userToCreate = UserDto.from(userRequest);

        UserDto createdUser = userService.createUser(userToCreate);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdUser);
    }

    @ExceptionHandler(UsernameAlreadyTakenException.class)
    public ResponseEntity<Object> handleException(UsernameAlreadyTakenException e) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(e.getMessage());
    }
}
