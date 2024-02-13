package com.hrusch.webapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hrusch.webapp.model.Track;
import com.hrusch.webapp.controller.response.ApiError;
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

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = TimeController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
class TimeControllerWebLayerTest {
/*
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
        RequestBuilder requestBuilder = buildAddTimeRequest(requestModel);

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
                .thenThrow(new UserIdNotFoundException(requestModel.getUserId()));
        RequestBuilder requestBuilder = buildAddTimeRequest(requestModel);

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
        RequestBuilder requestBuilder = buildAddTimeRequest(requestModel);

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        ApiError apiError = objectMapper
                .readValue(mvcResult.getResponse().getContentAsString(), ApiError.class);

        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(apiError.getSubErrors()).hasSize(3);
    }

    private RequestBuilder buildAddTimeRequest(TimeRequest requestModel) throws Exception {
        return MockMvcRequestBuilders.post(ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestModel));
    }

    @Test
    void getBestTimes_whenNoTimesInDatabase_return204HttpCode() throws Exception {
        when(timeService.getBestTimes()).thenReturn(Collections.emptyList());
        RequestBuilder requestBuilder = buildGetBestTimesRequest(null);

        MvcResult mvcResult = mockMvc.perform(requestBuilder)
                .andExpect(status().isNoContent())
                .andReturn();

        assertThat(mvcResult.getResponse().getContentAsString()).isEmpty();
    }

    @Test
    void getBestTimes_whenNoTimesForUserInDatabase_return204HttpCode() throws Exception {
        when(timeService.getBestTimes(anyString())).thenReturn(Collections.emptyList());
        RequestBuilder requestBuilder = buildGetBestTimesRequest("username");

        MvcResult mvcResult = mockMvc.perform(requestBuilder)
                .andExpect(status().isNoContent())
                .andReturn();

        assertThat(mvcResult.getResponse().getContentAsString()).isEmpty();
    }

    @Test
    void getBestTimes_whenTimesInDatabase_returnList() throws Exception {
        when(timeService.getBestTimes()).thenReturn(List.of(createTimeDto(UUID.randomUUID())));
        RequestBuilder requestBuilder = buildGetBestTimesRequest(null);

        MvcResult mvcResult = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();
        TimeDto[] times = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), TimeDto[].class);

        assertThat(times).hasSize(1);
    }

    @Test
    void getBestTimes_whenTimesForUserInDatabase_returnList() throws Exception {
        when(timeService.getBestTimes(anyString())).thenReturn(List.of(createTimeDto(UUID.randomUUID())));
        RequestBuilder requestBuilder = buildGetBestTimesRequest("Username");

        MvcResult mvcResult = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();
        TimeDto[] times = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), TimeDto[].class);

        assertThat(times).hasSize(1);
    }

    private RequestBuilder buildGetBestTimesRequest(String username) {
        var requestBuilder = MockMvcRequestBuilders.get(ENDPOINT + "/best");

        if (username != null) {
            requestBuilder.param("username", username);
        }

        return requestBuilder;
    }

    @Test
    void getBestTime_whenNoTimesInDatabase_return204HttpCode() throws Exception {
        var track = Track.WATER_PARK;
        when(timeService.getBestTimeForTrack(any(Track.class))).thenReturn(Optional.empty());
        RequestBuilder requestBuilder = buildGetBestTimeRequest(track, null);

        MvcResult mvcResult = mockMvc.perform(requestBuilder)
                .andExpect(status().isNoContent())
                .andReturn();

        assertThat(mvcResult.getResponse().getContentAsString()).isEmpty();
    }

    @Test
    void getBestTime_whenNoTimesForUserInDatabase_return204HttpCode() throws Exception {
        var track = Track.WATER_PARK;
        when(timeService.getBestTimeForTrack(any(Track.class), anyString())).thenReturn(Optional.empty());
        RequestBuilder requestBuilder = buildGetBestTimeRequest(track, "username");

        MvcResult mvcResult = mockMvc.perform(requestBuilder)
                .andExpect(status().isNoContent())
                .andReturn();

        assertThat(mvcResult.getResponse().getContentAsString()).isEmpty();
    }

    @Test
    void getBestTime_whenTimesInDatabase_returnTime() throws Exception {
        var track = Track.WATER_PARK;
        when(timeService.getBestTimeForTrack(any(Track.class))).thenReturn(Optional.of(createTimeDto(UUID.randomUUID())));
        RequestBuilder requestBuilder = buildGetBestTimeRequest(track, null);

        MvcResult mvcResult = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();
        TimeDto time = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), TimeDto.class);

        assertThat(time).isNotNull();
    }

    @Test
    void getBestTime_whenTimesForUserInDatabase_returnTime() throws Exception {
        var track = Track.WATER_PARK;
        when(timeService.getBestTimeForTrack(any(Track.class), anyString())).thenReturn(Optional.of(createTimeDto(UUID.randomUUID())));
        RequestBuilder requestBuilder = buildGetBestTimeRequest(track, "Username");

        MvcResult mvcResult = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();
        TimeDto time = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), TimeDto.class);

        assertThat(time).isNotNull();
    }

    private RequestBuilder buildGetBestTimeRequest(Track track, String username) {
        var requestBuilder = MockMvcRequestBuilders.get(ENDPOINT + "/best/" + track.name());

        if (username != null) {
            requestBuilder.param("username", username);
        }

        return requestBuilder;
    }
    */
}
