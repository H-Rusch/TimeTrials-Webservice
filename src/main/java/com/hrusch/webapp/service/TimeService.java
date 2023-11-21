package com.hrusch.webapp.service;

import com.hrusch.webapp.exception.UserDoesNotExistException;
import com.hrusch.webapp.model.dto.TimeDto;

public interface TimeService {

    TimeDto saveTime(TimeDto timeDto) throws UserDoesNotExistException;
}
