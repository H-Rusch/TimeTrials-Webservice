package com.hrusch.timetrials.webservice.controller;

import com.hrusch.openapi.api.TimesApi;
import com.hrusch.openapi.model.MkApiTimeRequest;
import com.hrusch.openapi.model.MkApiTimeResponse;
import com.hrusch.timetrials.webservice.mapper.TimeMapper;
import com.hrusch.timetrials.webservice.model.TimeDto;
import com.hrusch.timetrials.webservice.model.Track;
import com.hrusch.timetrials.webservice.model.serialization.TrackStringToTrackConverter;
import com.hrusch.timetrials.webservice.service.TimeService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequiredArgsConstructor
public class TimeController implements TimesApi {

  private final TimeService timeService;
  private final TimeMapper timeMapper;
  private final TrackStringToTrackConverter trackConverter;

  @Override
  public ResponseEntity<List<MkApiTimeResponse>> getBestTimeForEachTrack(String username) {
    List<MkApiTimeResponse> times = timeService.getBestTimeForEachTrack(username).stream()
        .map(timeMapper::map)
        .toList();

    if (times.isEmpty()) {
      return ResponseEntity.noContent().build();
    }
    return ResponseEntity.ok(times);
  }

  @Override
  public ResponseEntity<MkApiTimeResponse> getBestTimeForTrack(
      String trackIdentifier,
      String username) {
    Track track = trackConverter.convert(trackIdentifier);

    return timeService.getBestTimeForTrack(track, username)
        .map(timeMapper::map)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.noContent().build());
  }

  @Override
  public ResponseEntity<Void> saveNewTime(MkApiTimeRequest timeRequest) {
    TimeDto timeDto = timeMapper.map(timeRequest);
    timeService.saveNewTime(timeDto);

    return ResponseEntity.status(HttpStatus.CREATED).build();
  }
}
