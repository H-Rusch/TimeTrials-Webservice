package com.hrusch.webapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hrusch.webapp.exception.UsernameAlreadyTakenException;
import com.hrusch.webapp.model.dto.UserDto;
import com.hrusch.webapp.model.errorResponse.ApiError;
import com.hrusch.webapp.model.request.UserRequest;
import com.hrusch.webapp.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
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

import static com.hrusch.webapp.UserUtil.createUserDtoFromRequestModel;
import static com.hrusch.webapp.UserUtil.createUserRequestModel;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = UserController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
class UserControllerWebLayerTest {

    private static final String ENDPOINT = "/users";

    @MockBean
    UserService userService;
    @MockBean
    ModelMapper modelMapper;

    @Autowired
    private MockMvc mockMvc;

    UserRequest requestModel;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        requestModel = createUserRequestModel();
        when(modelMapper.map(any(), any()))
                .thenReturn(createUserDtoFromRequestModel(requestModel));
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void createUser_whenValidUserDetailsProvided_returnsCreatedUserDetails() throws Exception {
        when(userService.createUser(any(UserDto.class)))
                .thenReturn(createUserDtoFromRequestModel(requestModel));
        RequestBuilder requestBuilder = buildRequest(requestModel);

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        UserDto createdUser = objectMapper
                .readValue(mvcResult.getResponse().getContentAsString(), UserDto.class);

        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(HttpStatus.CREATED.value());
        assertEquals(requestModel.getUsername(), createdUser.getUsername());
        assertNotNull(createdUser.getUserId());
        assertFalse(createdUser.getUserId().isEmpty());
    }

    @Test
    void createUser_whenUserDetailsWithNonUniqueUsernameProvided_returnsErrorResponse() throws Exception {
        UserDto dto = createUserDtoFromRequestModel(requestModel);
        when(userService.createUser(any(UserDto.class)))
                .thenThrow(new UsernameAlreadyTakenException(dto.getUsername()));
        RequestBuilder requestBuilder = buildRequest(requestModel);

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        ApiError apiError = objectMapper
                .readValue(mvcResult.getResponse().getContentAsString(), ApiError.class);

        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(HttpStatus.CONFLICT.value());
        assertThat(apiError.getSubErrors()).isNull();
        assertThat(apiError.getMessage()).startsWith("The username has already been taken");
    }

    @Test
    void createUser_whenUserDetailsDoNotPassValidation_returnsErrorResponse() throws Exception {
        requestModel.setPassword("a");
        RequestBuilder requestBuilder = buildRequest(requestModel);

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        ApiError apiError = objectMapper
                .readValue(mvcResult.getResponse().getContentAsString(), ApiError.class);

        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(apiError.getSubErrors()).hasSize(3);
    }

    private RequestBuilder buildRequest(UserRequest requestModel) throws Exception {
        return MockMvcRequestBuilders.post(ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestModel));
    }
}
