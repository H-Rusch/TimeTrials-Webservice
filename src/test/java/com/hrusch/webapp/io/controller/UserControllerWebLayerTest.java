package com.hrusch.webapp.io.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hrusch.webapp.common.UserDto;
import com.hrusch.webapp.exception.UsernameAlreadyTakenException;
import com.hrusch.webapp.io.request.UserRequest;
import com.hrusch.webapp.io.response.UserResponse;
import com.hrusch.webapp.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.hrusch.webapp.io.controller.UserUtil.createUserDtoFromRequestModel;
import static com.hrusch.webapp.io.controller.UserUtil.createUserRequestModel;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
class UserControllerWebLayerTest {

    private static final String ENDPOINT = "/users";

    @MockBean
    UserService userService;

    @Autowired
    private MockMvc mockMvc;

    UserRequest requestModel;

    @BeforeEach
    void setUp() {
        requestModel = createUserRequestModel();
    }

    @Test
    void createUser_whenValidUserDetailsProvided_returnsCreatedUserDetails() throws Exception {
        when(userService.createUser(any(UserDto.class)))
                .thenReturn(createUserDtoFromRequestModel(requestModel));
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(requestModel));

        MvcResult mvcResult = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();
        String responseBodyString = mvcResult.getResponse().getContentAsString();
        UserResponse createdUser = new ObjectMapper()
                .readValue(responseBodyString, UserResponse.class);

        assertEquals(requestModel.getUsername(), createdUser.getUsername());
        assertNotNull(createdUser.getUserId());
        assertFalse(createdUser.getUserId().isEmpty());
    }

    @Test
    void createUser_whenUserDetailsWithNonUniqueUsernameProvided_returnsErrorResponse() throws Exception {
        UserDto dto = createUserDtoFromRequestModel(requestModel);
        when(userService.createUser(any(UserDto.class)))
                .thenThrow(new UsernameAlreadyTakenException(dto.getUsername()));
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(requestModel));

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(HttpStatus.CONFLICT.value());
        assertThat(mvcResult.getResponse().getContentAsString()).startsWith("The username has already been taken");
    }
}
