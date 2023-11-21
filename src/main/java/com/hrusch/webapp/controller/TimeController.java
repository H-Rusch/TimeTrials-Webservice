package com.hrusch.webapp.controller;

import com.hrusch.webapp.exception.UserDoesNotExistException;
import com.hrusch.webapp.model.dto.TimeDto;
import com.hrusch.webapp.model.request.TimeRequest;
import com.hrusch.webapp.service.TimeService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

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

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdTime);
    }

    TimeDto convertToTimeDto(TimeRequest timeRequest) {
        TimeDto timeDto = modelMapper.map(timeRequest, TimeDto.class);
        timeDto.setCreatedAt(LocalDateTime.now());

        return timeDto;
    }
}
