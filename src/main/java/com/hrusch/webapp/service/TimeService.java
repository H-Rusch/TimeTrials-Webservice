package com.hrusch.webapp.service;

import com.hrusch.webapp.common.TimeDto;
import com.hrusch.webapp.exception.UserDoesNotExistException;

public interface TimeService {

    TimeDto saveTime(TimeDto timeDto) throws UserDoesNotExistException;
}
