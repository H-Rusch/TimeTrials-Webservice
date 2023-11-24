package com.hrusch.webapp.service;

import com.hrusch.webapp.error.exception.UserDoesNotExistException;
import com.hrusch.webapp.model.Track;
import com.hrusch.webapp.model.dto.TimeDto;

import java.util.List;
import java.util.Optional;

public interface TimeService {

    TimeDto saveTime(TimeDto timeDto) throws UserDoesNotExistException;

    List<TimeDto> getBestTimes();

    List<TimeDto> getBestTimes(String username) throws UserDoesNotExistException;

    Optional<TimeDto> getBestTimeForTrack(Track track);

    Optional<TimeDto> getBestTimeForTrack(Track track, String username) throws UserDoesNotExistException;
}
