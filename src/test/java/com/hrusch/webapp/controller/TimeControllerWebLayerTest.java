package com.hrusch.webapp.controller;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hrusch.webapp.config.JacksonConfig;
import com.hrusch.webapp.model.Time;
import com.hrusch.webapp.model.Track;
import com.hrusch.webapp.service.TimeService;
import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(controllers = TimeController.class, excludeAutoConfiguration = {
    SecurityAutoConfiguration.class})
class TimeControllerWebLayerTest {

  private static final String ENDPOINT = "/times";

  @MockBean
  TimeService timeService;

  @Autowired
  private MockMvc mockMvc;

  private ObjectMapper objectMapper;

  @BeforeEach
  void setUp() {
    objectMapper = new JacksonConfig().objectMapper();
  }

  @Nested
  class TimeControllerGetBestTimeForEachTrackWebLayerTest {

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
      Time time = createSampleTime();
      time.setUsername(null);
      when(timeService.getBestTimeForEachTrack(time.getUsername()))
          .thenReturn(List.of(time));
      RequestBuilder requestBuilder = buildGetBestTimesRequest(time.getUsername());

      // when
      MvcResult mvcResult = mockMvc.perform(requestBuilder)
          .andExpect(status().isOk())
          .andReturn();
      Time[] times = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
          Time[].class);

      // then
      assertThat(times)
          .hasSize(1);
    }

    @Test
    void givenGetBestTimeForEachTrackRequestWithUsername_whenTimesInDatabase_returnList()
        throws Exception {
      // given
      Time time = createSampleTime();
      when(timeService.getBestTimeForEachTrack(anyString()))
          .thenReturn(List.of(time));
      RequestBuilder requestBuilder = buildGetBestTimesRequest(time.getUsername());

      // when
      MvcResult mvcResult = mockMvc.perform(requestBuilder)
          .andExpect(status().isOk())
          .andReturn();
      Time[] times = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
          Time[].class);

      // then
      assertThat(times)
          .hasSize(1)
          .contains(time);
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
  class TimeControllerGetBestTimeForTrackWebLayerTest {

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
    void givenGetBestTimeRequest_whenInvalidTrackGiven_then() throws Exception {
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
      Time time = createSampleTime();
      when(timeService.getBestTimeForTrack(time.getTrack(), time.getUsername()))
          .thenReturn(Optional.of(time));
      RequestBuilder requestBuilder = buildGetBestTimeForTrackRequest(
          time.getTrack(),
          time.getUsername());

      // when
      MvcResult mvcResult = mockMvc.perform(requestBuilder)
          .andExpect(status().isOk())
          .andReturn();
      Time times = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
          Time.class);

      // then
      assertThat(times)
          .isEqualTo(time);
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

  private Time createSampleTime() {
    return Time.builder()
        .username("username")
        .track(Track.BABY_PARK_GCN)
        .duration(Duration.parse("PT1M4.78S"))
        .build();
  }
}
