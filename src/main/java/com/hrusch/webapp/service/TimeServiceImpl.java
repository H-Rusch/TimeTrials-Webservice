package com.hrusch.webapp.service;

import com.hrusch.webapp.model.TimeDto;
import com.hrusch.webapp.exception.UserDoesNotExistException;
import com.hrusch.webapp.repository.TimeEntity;
import com.hrusch.webapp.repository.TimeRepository;
import com.hrusch.webapp.repository.UserEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TimeServiceImpl implements TimeService {

    private static final Logger LOG = LoggerFactory.getLogger(TimeServiceImpl.class);

    private final UserService userService;
    private final TimeRepository timeRepository;

    @Autowired
    public TimeServiceImpl(UserService userService, TimeRepository timeRepository) {
        this.userService = userService;
        this.timeRepository = timeRepository;
    }

    @Override
    public TimeDto saveTime(TimeDto timeDto) throws UserDoesNotExistException {
        UserEntity userEntity = userService.findUserByUserId(timeDto.getUserId());
        TimeEntity timeEntity = TimeEntity.from(timeDto, userEntity);

        TimeEntity createdTime = timeRepository.save(timeEntity);
        LOG.info("Saved new time for user with user-id: {}", createdTime.getUser().getUserId());

        return TimeDto.from(timeEntity);
    }
}
