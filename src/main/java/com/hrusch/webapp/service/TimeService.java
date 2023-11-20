package com.hrusch.webapp.service;

import com.hrusch.webapp.model.dto.TimeDto;
import com.hrusch.webapp.exception.UserDoesNotExistException;

public interface TimeService {

    TimeDto saveTime(TimeDto timeDto) throws UserDoesNotExistException;
}
