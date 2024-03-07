package com.hrusch.timetrials.webservice.service;

import com.hrusch.timetrials.webservice.repository.TimeRepository;
import com.hrusch.timetrials.webservice.exception.ParameterErrorException;
import com.hrusch.timetrials.webservice.model.Time;
import com.hrusch.timetrials.webservice.model.TimeDto;
import com.hrusch.timetrials.webservice.model.Track;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TimeServiceImpl implements TimeService {

  private final TimeRepository timeRepository;
  private final RecordKeeperService recordKeeperService;
  private final ModelMapper modelMapper;

  @Autowired
  public TimeServiceImpl(TimeRepository timeRepository,
      RecordKeeperService recordKeeperService,
      ModelMapper modelMapper) {
    this.timeRepository = timeRepository;
    this.recordKeeperService = recordKeeperService;
    this.modelMapper = modelMapper;
  }

  @Override
  public Collection<Time> getBestTimeForEachTrack(String username) {
    return timeRepository.findBestTimeForEachTrack(username);
  }

  @Override
  public Optional<Time> getBestTimeForTrack(Track track, String username) {
    return Optional.ofNullable(track)
        .map(it -> timeRepository.findBestTimeForTrack(it, username))
        .orElseThrow(() -> new ParameterErrorException("track"));
  }

  @Override
  public void saveNewTime(TimeDto timeDto) {
    Time time = convertToTime(timeDto);

    Time savedTime = timeRepository.saveTime(time);

    recordKeeperService.update(savedTime);
  }

  private Time convertToTime(TimeDto timeDto) {
    Time time = modelMapper.map(timeDto, Time.class);
    time.setCreatedAt(LocalDateTime.now());

    return time;
  }
}
