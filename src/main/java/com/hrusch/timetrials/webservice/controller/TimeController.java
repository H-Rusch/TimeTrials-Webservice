package com.hrusch.timetrials.webservice.controller;

import com.hrusch.openapi.api.TimesApi;
import com.hrusch.openapi.model.MkApiTimeRequest;
import com.hrusch.openapi.model.MkApiTimeResponseEntry;
import com.hrusch.timetrials.webservice.mapper.TimeMapper;
import com.hrusch.timetrials.webservice.model.TimeDto;
import com.hrusch.timetrials.webservice.model.Track;
import com.hrusch.timetrials.webservice.model.serialization.TrackStringToTrackConverter;
import com.hrusch.timetrials.webservice.service.TimeService;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
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
  public ResponseEntity<Map<String, List<MkApiTimeResponseEntry>>> getBestTimeForEachTrack(
      String username) {
    var timesByTrack = timeService.getBestTimeForEachTrack(username)
        .stream()
        .flatMap(List::stream)
        .map(timeMapper::map)
        .collect(Collectors.groupingBy(MkApiTimeResponseEntry::getTrack));

    if (timesByTrack.isEmpty()) {
      return ResponseEntity.noContent().build();
    }
    return ResponseEntity.ok(timesByTrack);
  }

  @Override
  public ResponseEntity<List<MkApiTimeResponseEntry>> getBestTimeForTrack(
      String trackIdentifier,
      String username) {
    Track track = trackConverter.convert(trackIdentifier);

    var timeResponseEntries = timeService.getBestTimeForTrack(track, username)
        .stream()
        .map(timeMapper::map)
        .toList();

    return timeResponseEntries.isEmpty()
        ? ResponseEntity.noContent().build()
        : ResponseEntity.ok(timeResponseEntries);
  }

  @Override
  public ResponseEntity<Void> saveNewTime(MkApiTimeRequest timeRequest) {
    TimeDto timeDto = timeMapper.map(timeRequest);
    timeService.saveNewTime(timeDto);

    return ResponseEntity.status(HttpStatus.CREATED).build();
  }
}
