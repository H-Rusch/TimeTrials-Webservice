package com.hrusch.webapp.service;

import com.hrusch.webapp.exception.UserDoesNotExistException;
import com.hrusch.webapp.model.TimeEntity;
import com.hrusch.webapp.model.UserEntity;
import com.hrusch.webapp.model.dto.TimeDto;
import com.hrusch.webapp.repository.TimeRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TimeServiceImpl implements TimeService {

    private static final Logger LOG = LoggerFactory.getLogger(TimeServiceImpl.class);

    private final UserService userService;
    private final TimeRepository timeRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public TimeServiceImpl(UserService userService, TimeRepository timeRepository, ModelMapper modelMapper) {
        this.userService = userService;
        this.timeRepository = timeRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public TimeDto saveTime(TimeDto timeDto) throws UserDoesNotExistException {
        UserEntity userEntity = userService.findUserByUserId(timeDto.getUserId());
        TimeEntity timeEntity = convertToTimeEntity(timeDto, userEntity);

        TimeEntity createdTime = timeRepository.save(timeEntity);
        LOG.info("Saved new time for user with user-id: {}", createdTime.getUser().getUserId());

        return convertToTimeDto(timeEntity);
    }

    TimeEntity convertToTimeEntity(TimeDto timeDto, UserEntity userEntity) {
        TimeEntity timeEntity = modelMapper.map(timeDto, TimeEntity.class);
        timeEntity.setUser(userEntity);

        return timeEntity;
    }

    TimeDto convertToTimeDto(TimeEntity timeEntity) {
        TimeDto timeDto = modelMapper.map(timeEntity, TimeDto.class);
        timeDto.setUsername(timeEntity.getUser().getUsername());
        timeDto.setUserId(timeEntity.getUser().getUserId());

        return timeDto;
    }
}
