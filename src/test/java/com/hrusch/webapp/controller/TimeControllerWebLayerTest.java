package com.hrusch.webapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hrusch.webapp.TimeUtil;
import com.hrusch.webapp.exception.UserDoesNotExistException;
import com.hrusch.webapp.model.Track;
import com.hrusch.webapp.model.dto.TimeDto;
import com.hrusch.webapp.model.errorResponse.ApiError;
import com.hrusch.webapp.model.request.TimeRequest;
import com.hrusch.webapp.service.TimeService;
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

import static com.hrusch.webapp.TimeUtil.createTimeDtoFromRequestModel;
import static com.hrusch.webapp.TimeUtil.createTimeRequestModel;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = TimeController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
class TimeControllerWebLayerTest {

    private static final String ENDPOINT = "/times";

    @MockBean
    TimeService timeService;
    @MockBean
    ModelMapper modelMapper;

    @Autowired
    private MockMvc mockMvc;

    TimeRequest requestModel;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        requestModel = createTimeRequestModel();
        var returnedDto = createTimeDtoFromRequestModel(requestModel);
        when(modelMapper.map(any(TimeRequest.class), eq(TimeDto.class)))
                .thenReturn(returnedDto);
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void saveTime_whenValidTimeRequestProvided_returnSavedTimeDetails() throws Exception {
        var returnedDto = createTimeDtoFromRequestModel(requestModel);
        returnedDto.setUsername("Testing 123");
        when(timeService.saveTime(any(TimeDto.class)))
                .thenReturn(returnedDto);
        RequestBuilder requestBuilder = buildRequest(requestModel);

        MvcResult mvcResult = mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();
        TimeDto savedTime = objectMapper
                .readValue(mvcResult.getResponse().getContentAsString(), TimeDto.class);

        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(savedTime.getTrack()).isEqualTo(Track.BABY_PARK_GCN);
        assertThat(savedTime.getTime()).isEqualTo(TimeUtil.VALID_DURATION);
        assertThat(savedTime.getUsername()).isNotEmpty();
        assertThat(savedTime.getCreatedAt()).isNotNull();
    }

    @Test
    void saveTime_whenTimeRequestForNotPresentUserProvided_returnError() throws Exception {
        when(timeService.saveTime(any(TimeDto.class)))
                .thenThrow(new UserDoesNotExistException(requestModel.getUserId()));
        RequestBuilder requestBuilder = buildRequest(requestModel);

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        ApiError apiError = objectMapper
                .readValue(mvcResult.getResponse().getContentAsString(), ApiError.class);

        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(HttpStatus.CONFLICT.value());
        assertThat(apiError.getMessage()).startsWith("User with the userId");
        assertThat(apiError.getMessage()).endsWith("does not exist.");
        assertThat(apiError.getSubErrors()).isNull();
    }

    @Test
    void saveTime_whenInvalidTimeRequestProvided_returnError() throws Exception {
        requestModel.setTrack(null);
        requestModel.setTime(null);
        requestModel.setUserId(null);
        RequestBuilder requestBuilder = buildRequest(requestModel);

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        ApiError apiError = objectMapper
                .readValue(mvcResult.getResponse().getContentAsString(), ApiError.class);

        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(apiError.getSubErrors()).hasSize(3);
    }

    private RequestBuilder buildRequest(TimeRequest requestModel) throws Exception {
        return MockMvcRequestBuilders.post(ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestModel));
    }
}
