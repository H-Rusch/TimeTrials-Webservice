package com.hrusch.webapp.controller;

import com.hrusch.webapp.model.Time;
import com.hrusch.webapp.model.Track;
import com.hrusch.webapp.service.TimeService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController()
@RequestMapping("/times")
public class TimeController {

    private final TimeService timeService;

    @Autowired
    public TimeController(TimeService timeService, ModelMapper modelMapper) {
        this.timeService = timeService;
    }

    /**
     * Get the best time for each track from the database.
     * Optionally a user can be specified, in order to get the best times for that user.
     */
    @GetMapping(path = "/best")
    public ResponseEntity<List<Time>> getBestTimes(String username) {
        List<Time> times = timeService.getBestTimes(username);

        if (times.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(times);
        }
    }

    /**
     * Get the best time for a track.
     * Optionally a user can be specified, in order to get the best time on that track for that specific user.
     */
    @GetMapping(path = "/best/{track}")
    public ResponseEntity<Time> getBestTime(
            @PathVariable Track track,
            @RequestParam(required = false) String username) {
        return timeService.getBestTimeForTrack(track, username)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.noContent().build());
    }
}
