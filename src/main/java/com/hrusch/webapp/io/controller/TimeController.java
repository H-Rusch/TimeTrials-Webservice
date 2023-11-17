package com.hrusch.webapp.io.controller;

import com.hrusch.webapp.common.TimeDto;
import com.hrusch.webapp.exception.UserDoesNotExistException;
import com.hrusch.webapp.io.request.TimeRequest;
import com.hrusch.webapp.io.response.TimeResponse;
import com.hrusch.webapp.service.TimeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController()
@RequestMapping("/times")
public class TimeController {

    private final TimeService timeService;

    @Autowired
    public TimeController(TimeService timeService) {
        this.timeService = timeService;
    }

    /**
     * POST endpoint for uploading a new time.
     *
     * @param timeRequest the time to save in the database
     * @return a representation of the created time record
     */
    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public TimeResponse saveTime(@RequestBody @Valid TimeRequest timeRequest) throws UserDoesNotExistException {
        TimeDto timeDto = TimeDto.from(timeRequest);

        TimeDto createdTime = timeService.saveTime(timeDto);

        return TimeResponse.from(createdTime);
    }

    @ExceptionHandler(UserDoesNotExistException.class)
    public ResponseEntity<Object> handleException(UserDoesNotExistException e) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(e.getMessage());
    }
}
