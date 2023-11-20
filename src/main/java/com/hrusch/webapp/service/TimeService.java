package com.hrusch.webapp.service;

import com.hrusch.webapp.model.TimeDto;
import com.hrusch.webapp.exception.UserDoesNotExistException;

public interface TimeService {

    TimeDto saveTime(TimeDto timeDto) throws UserDoesNotExistException;
}
