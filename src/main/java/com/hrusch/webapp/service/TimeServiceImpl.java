package com.hrusch.webapp.service;

import com.hrusch.webapp.model.Time;
import com.hrusch.webapp.model.Track;
import java.util.List;
import java.util.Optional;

public class TimeServiceImpl implements TimeService {

  @Override
  public Time saveTime(Time timeDto) {
    return null;
  }

  @Override
  public List<Time> getBestTimes(String username) {
    return null;
  }

  @Override
  public Optional<Time> getBestTimeForTrack(Track track, String username) {
    return Optional.empty();
  }
}
