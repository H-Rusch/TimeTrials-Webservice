package com.hrusch.webapp.service;

import com.hrusch.webapp.exception.ParameterErrorException;
import com.hrusch.webapp.model.Time;
import com.hrusch.webapp.model.Track;
import com.hrusch.webapp.repository.TimeRepository;
import java.util.Collection;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class TimeServiceImpl implements TimeService {

  private final TimeRepository timeRepository;

  public TimeServiceImpl(TimeRepository timeRepository) {
    this.timeRepository = timeRepository;
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
}
