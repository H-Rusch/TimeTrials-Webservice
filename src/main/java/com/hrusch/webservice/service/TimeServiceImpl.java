package com.hrusch.webservice.service;

import com.hrusch.webservice.exception.ParameterErrorException;
import com.hrusch.webservice.model.Time;
import com.hrusch.webservice.model.TimeDto;
import com.hrusch.webservice.model.Track;
import com.hrusch.webservice.repository.TimeRepository;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TimeServiceImpl implements TimeService {

  private final TimeRepository timeRepository;
  private final ModelMapper modelMapper;

  @Autowired
  public TimeServiceImpl(TimeRepository timeRepository, ModelMapper modelMapper) {
    this.timeRepository = timeRepository;
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

    timeRepository.saveTime(time);
  }

  private Time convertToTime(TimeDto timeDto) {
    Time time = modelMapper.map(timeDto, Time.class);
    time.setCreatedAt(LocalDateTime.now());

    return time;
  }
}
