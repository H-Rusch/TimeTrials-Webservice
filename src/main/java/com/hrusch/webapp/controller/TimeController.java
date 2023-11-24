package com.hrusch.webapp.controller;

import com.hrusch.webapp.error.exception.UserDoesNotExistException;
import com.hrusch.webapp.model.Track;
import com.hrusch.webapp.model.dto.TimeDto;
import com.hrusch.webapp.model.request.TimeRequest;
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
    private final ModelMapper modelMapper;

    @Autowired
    public TimeController(TimeService timeService, ModelMapper modelMapper) {
        this.timeService = timeService;
        this.modelMapper = modelMapper;
    }

    /**
     * POST endpoint for uploading a new time.
     *
     * @param timeRequest the time to save in the database
     * @return a representation of the created time record
     */
    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<TimeDto> saveTime(@RequestBody @Valid TimeRequest timeRequest) throws UserDoesNotExistException {
        TimeDto timeDto = convertToTimeDto(timeRequest);

        TimeDto createdTime = timeService.saveTime(timeDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdTime);
    }

    /**
     * Get the best time for each track from the database.
     * Optionally a user can be specified, in order to get the best times for that user.
     */
    @GetMapping(path = "/best")
    public ResponseEntity<List<TimeDto>> getBestTimes(@RequestParam(required = false) String username) throws UserDoesNotExistException {
        List<TimeDto> times = username == null ? timeService.getBestTimes() : timeService.getBestTimes(username);

        if (times.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(times);
        }
    }

    /**
     * Get the best time for a track.
     * Optionally a user can be specified, in order to get the best time on that track for that specific user.
     */
    @GetMapping(path = "/best/{track}")
    public ResponseEntity<TimeDto> getBestTime(
            @PathVariable Track track,
            @RequestParam(required = false) String username
    ) throws UserDoesNotExistException {
        Optional<TimeDto> time = username == null ? timeService.getBestTimeForTrack(track) : timeService.getBestTimeForTrack(track, username);

        return time.map(timeDto -> ResponseEntity.status(HttpStatus.OK).body(timeDto))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NO_CONTENT).body(null));
    }


    TimeDto convertToTimeDto(TimeRequest timeRequest) {
        TimeDto timeDto = modelMapper.map(timeRequest, TimeDto.class);
        timeDto.setCreatedAt(LocalDateTime.now());

        return timeDto;
    }
}
