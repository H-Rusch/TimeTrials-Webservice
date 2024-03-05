package com.hrusch.timetrials.webservice.controller;

import com.hrusch.timetrials.webservice.model.Time;
import com.hrusch.timetrials.webservice.model.TimeDto;
import com.hrusch.timetrials.webservice.model.Track;
import com.hrusch.timetrials.webservice.service.TimeService;
import jakarta.validation.Valid;
import java.util.Collection;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/times")
public class TimeController {

  private final TimeService timeService;

  public TimeController(TimeService timeService) {
    this.timeService = timeService;
  }

  /**
   * Get the best time for each track from the database. Optionally a username can be specified, in
   * order to get the best times for that specific user.
   */
  @GetMapping(path = "/best")
  public ResponseEntity<List<Time>> getBestTimeForEachTrack(
      @RequestParam(required = false) String username) {
    Collection<Time> times = timeService.getBestTimeForEachTrack(username);

    if (times.isEmpty()) {
      return ResponseEntity.noContent().build();
    } else {
      return ResponseEntity.ok(times.stream().toList());
    }
  }

  /**
   * Get the best time for a track. Optionally a username can be specified, in order to get the best
   * time on that track for that specific user.
   */
  @GetMapping(path = "/best/{track}")
  public ResponseEntity<Time> getBestTimeForTrack(
      @PathVariable Track track,
      @RequestParam(required = false) String username) {
    return timeService.getBestTimeForTrack(track, username)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.noContent().build());
  }

  /**
   * Save a new time to the database.
   *
   * @return ResponseEntity with a 201 status code signalling, the new time was created
   * successfully.
   */
  @PostMapping
  public ResponseEntity<Void> saveNewTime(@RequestBody @Valid TimeDto timeDto) {
    timeService.saveNewTime(timeDto);

    return ResponseEntity.status(HttpStatus.CREATED).build();
  }
}
