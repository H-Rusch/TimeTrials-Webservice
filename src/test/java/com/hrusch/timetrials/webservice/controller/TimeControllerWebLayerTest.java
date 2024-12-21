package com.hrusch.timetrials.webservice.controller;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hrusch.openapi.model.MkApiErrorResponse;
import com.hrusch.openapi.model.MkApiTimeResponse;
import com.hrusch.timetrials.webservice.config.JacksonConfig;
import com.hrusch.timetrials.webservice.mapper.TimeMapper;
import com.hrusch.timetrials.webservice.model.TimeDto;
import com.hrusch.timetrials.webservice.model.Track;
import com.hrusch.timetrials.webservice.service.TimeService;
import com.hrusch.timetrials.webservice.testutils.MapperBeans;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(controllers = TimeController.class)
@Import({JacksonConfig.class, MapperBeans.class})
class TimeControllerWebLayerTest {

  private static final String ENDPOINT = "/times";

  @MockitoBean
  TimeService timeService;
  @MockitoSpyBean
  TimeMapper timeMapper;

  @Autowired
  private MockMvc mockMvc;

  private ObjectMapper objectMapper;

  @BeforeEach
  void setUp() {
    objectMapper = new JacksonConfig().objectMapper();
  }

  @Nested
  class TimeController_GetBestTimeForEachTrack_WebLayerTest {

    @Test
    void givenGetBestTimeForEachTrackRequestWithoutUsername_whenNoTimesInDatabase_thenReturn204HttpCode()
        throws Exception {
      // given
      when(timeService.getBestTimeForEachTrack(null))
          .thenReturn(Collections.emptyList());
      RequestBuilder requestBuilder = buildGetBestTimesRequest(null);

      // when & then
      MvcResult mvcResult = mockMvc.perform(requestBuilder)
          .andExpect(status().isNoContent())
          .andReturn();

      assertThat(mvcResult.getResponse().getContentAsString())
          .isEmpty();
    }

    @Test
    void givenGetBestTimeForEachTrackRequestWithUsername_whenNoTimesForUserInDatabase_return204HttpCode()
        throws Exception {
      // given
      when(timeService.getBestTimeForEachTrack(anyString()))
          .thenReturn(Collections.emptyList());
      RequestBuilder requestBuilder = buildGetBestTimesRequest("username");

      // when & then
      MvcResult mvcResult = mockMvc.perform(requestBuilder)
          .andExpect(status().isNoContent())
          .andReturn();

      assertThat(mvcResult.getResponse().getContentAsString())
          .isEmpty();
    }

    @Test
    void givenGetBestTimeForEachTrackRequestWithoutUsername_whenTimesInDatabase_returnList()
        throws Exception {
      // given
      TimeDto timeDto = createSampleTimeDto();
      when(timeService.getBestTimeForEachTrack(null))
          .thenReturn(List.of(timeDto));
      RequestBuilder requestBuilder = buildGetBestTimesRequest(null);

      // when
      MvcResult mvcResult = mockMvc.perform(requestBuilder)
          .andExpect(status().isOk())
          .andReturn();
      MkApiTimeResponse[] timeResponses = objectMapper.readValue(
          mvcResult.getResponse().getContentAsString(),
          MkApiTimeResponse[].class);

      // then
      assertThat(timeResponses)
          .hasSize(1);
    }

    @Test
    void givenGetBestTimeForEachTrackRequestWithUsername_whenTimesInDatabase_returnList()
        throws Exception {
      // given
      TimeDto timeDto = createSampleTimeDto();
      timeDto.setCreatedAt(LocalDateTime.now());
      when(timeService.getBestTimeForEachTrack(timeDto.getUsername()))
          .thenReturn(List.of(timeDto));
      RequestBuilder requestBuilder = buildGetBestTimesRequest(timeDto.getUsername());

      // when
      MvcResult mvcResult = mockMvc.perform(requestBuilder)
          .andExpect(status().isOk())
          .andReturn();
      MkApiTimeResponse[] timeResponses = objectMapper.readValue(
          mvcResult.getResponse().getContentAsString(),
          MkApiTimeResponse[].class);

      // then
      assertThat(timeResponses)
          .hasSize(1)
          .extracting(MkApiTimeResponse::getUsername)
          .containsExactly(timeDto.getUsername());
    }

    private static RequestBuilder buildGetBestTimesRequest(String username) {
      var requestBuilder = MockMvcRequestBuilders.get(ENDPOINT + "/best");

      if (!Objects.isNull(username)) {
        requestBuilder.param("username", username);
      }

      return requestBuilder;
    }
  }

  @Nested
  class TimeController_GetBestTimeForTrack_WebLayerTest {

    @Test
    void givenGetBestTimeRequest_whenTrackNotGiven_thenReturn400HttpCode() throws Exception {
      // given
      RequestBuilder requestBuilder = buildGetBestTimeForTrackRequest((Track) null, null);

      // when & then
      MvcResult mvcResult = mockMvc.perform(requestBuilder)
          .andExpect(status().isNotFound())
          .andReturn();

      assertThat(mvcResult.getResponse().getContentAsString())
          .contains("No static resource times/best.");
    }

    @Test
    void givenGetBestTimeRequest_whenInvalidTrackGiven_thenReturn400HttpCode() throws Exception {
      // given
      RequestBuilder requestBuilder = buildGetBestTimeForTrackRequest("invalid", null);

      // when & then
      MvcResult mvcResult = mockMvc.perform(requestBuilder)
          .andExpect(status().isBadRequest())
          .andReturn();

      assertThat(mvcResult.getResponse().getContentAsString())
          .contains("No 'Track' can be built from value 'invalid'.");
    }

    @Test
    void givenGetBestTimeRequest_whenNoTimeInDatabase_return204HttpCode() throws Exception {
      // given
      when(timeService.getBestTimeForTrack(any(Track.class), anyString()))
          .thenReturn(Optional.empty());
      RequestBuilder requestBuilder = buildGetBestTimeForTrackRequest(
          Track.BABY_PARK_GCN,
          "username");

      // when & then
      MvcResult mvcResult = mockMvc.perform(requestBuilder)
          .andExpect(status().isNoContent())
          .andReturn();

      assertThat(mvcResult.getResponse().getContentAsString())
          .isEmpty();
    }

    @Test
    void givenGetBestTimeRequest_whenTimesInDatabase_returnList() throws Exception {
      // given
      TimeDto timeDto = createSampleTimeDtoWithTimestamp();
      MkApiTimeResponse expectedResponse = timeMapper.map(timeDto);
      when(timeService.getBestTimeForTrack(timeDto.getTrack(), timeDto.getUsername()))
          .thenReturn(Optional.of(timeDto));
      RequestBuilder requestBuilder = buildGetBestTimeForTrackRequest(
          timeDto.getTrack(),
          timeDto.getUsername());

      // when & then
      MvcResult mvcResult = mockMvc.perform(requestBuilder)
          .andExpect(status().isOk())
          .andReturn();
      MkApiTimeResponse timeResponse = objectMapper.readValue(
          mvcResult.getResponse().getContentAsString(),
          MkApiTimeResponse.class);

      assertThat(timeResponse)
          .usingRecursiveComparison()
          .ignoringFields("createdAt")
          .isEqualTo(expectedResponse);
    }

    private static RequestBuilder buildGetBestTimeForTrackRequest(Track track, String username) {
      String trackString = Optional.ofNullable(track)
          .map(String::valueOf)
          .orElse(null);
      return buildGetBestTimeForTrackRequest(trackString, username);
    }

    private static RequestBuilder buildGetBestTimeForTrackRequest(String trackString,
        String username) {
      var requestBuilder = MockMvcRequestBuilders.get(ENDPOINT + "/best/{track}", trackString);

      if (!Objects.isNull(username)) {
        requestBuilder.param("username", username);
      }

      return requestBuilder;
    }
  }

  @Nested
  class TimeController_SaveNewTime_WebLayerTest {

    @Test
    void givenTimeDto_whenSavingToDatabase_then201Returned() throws Exception {
      // given
      RequestBuilder requestBuilder = buildPostNewTimeRequest(createSampleTimeDto());

      // when & then
      MvcResult mvcResult = mockMvc.perform(requestBuilder)
          .andExpect(status().isCreated())
          .andReturn();
      assertThat(mvcResult.getResponse().getContentAsString())
          .isEmpty();
    }

    @ParameterizedTest
    @MethodSource("invalidTimeDtoObjects")
    void givenInvalidTimeDto_whenSavingToDatabase_then400Returned(TimeDto timeDto)
        throws Exception {
      // given
      RequestBuilder requestBuilder = buildPostNewTimeRequest(timeDto);

      // when & then
      MvcResult mvcResult = mockMvc.perform(requestBuilder)
          .andExpect(status().isBadRequest())
          .andReturn();
      MkApiErrorResponse errorResponse = objectMapper.readValue(
          mvcResult.getResponse().getContentAsString(),
          MkApiErrorResponse.class);
      assertThat(errorResponse)
          .extracting(
              MkApiErrorResponse::getStatus,
              MkApiErrorResponse::getMessage)
          .containsExactly(
              HttpStatus.BAD_REQUEST.value(),
              "Validation failed");
    }

    @SneakyThrows
    private RequestBuilder buildPostNewTimeRequest(TimeDto timeDto) {
      String json = objectMapper.writeValueAsString(timeDto);

      return MockMvcRequestBuilders.post(ENDPOINT)
          .contentType(MediaType.APPLICATION_JSON)
          .content(json);
    }

    private static Stream<TimeDto> invalidTimeDtoObjects() {
      TimeDto usernameTooShort = createSampleTimeDto();
      usernameTooShort.setUsername("a");

      TimeDto usernameTooLong = createSampleTimeDto();
      usernameTooLong.setUsername("a".repeat(33));

      TimeDto usernameNull = createSampleTimeDto();
      usernameNull.setUsername(null);

      TimeDto durationNull = createSampleTimeDto();
      durationNull.setDuration(null);

      return Stream.of(
          usernameTooShort,
          usernameTooLong,
          usernameNull,
          durationNull);
    }
  }

  private static TimeDto createSampleTimeDtoWithTimestamp() {
    TimeDto timeDto = createSampleTimeDto();
    timeDto.setCreatedAt(LocalDateTime.now());

    return timeDto;
  }

  private static TimeDto createSampleTimeDto() {
    return TimeDto.builder()
        .username("username")
        .track(Track.BABY_PARK_GCN)
        .duration(Duration.parse("PT1M4.78S"))
        .build();
  }
}
