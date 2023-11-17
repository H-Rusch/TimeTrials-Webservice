package com.hrusch.webapp.io.controller;

import com.hrusch.webapp.common.UserDto;
import com.hrusch.webapp.io.request.UserRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.hrusch.webapp.UserUtil.createUserDtoFromRequestModel;
import static com.hrusch.webapp.UserUtil.createUserRequestModel;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @InjectMocks
    UserController userController;

    UserRequest requestModel;

    @BeforeEach
    void setUp() {
        requestModel = createUserRequestModel();
    }

    @Test
    void createUserDto_translatesTheRequestIntoCorrectDto() {
        var result = userController.createUserDto(requestModel);

        assertEquals(requestModel.getUsername(), result.getUsername());
        assertEquals(requestModel.getPassword(), result.getPassword());
        assertEquals("", result.getEncryptedPassword());
        assertNull(result.getId());
        assertNotNull(result.getUserId());
    }

    @Test
    void createUserResponse_translatesTheDtoIntoCorrectResponse() {
        UserDto dto = createUserDtoFromRequestModel(requestModel);

        var result = userController.createUserResponse(dto);

        assertEquals(dto.getUserId(), result.getUserId());
        assertEquals(dto.getUsername(), result.getUsername());
    }
}
