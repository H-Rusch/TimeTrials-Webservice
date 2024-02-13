package com.hrusch.webapp.repository;

import com.hrusch.webapp.model.Time;
import com.hrusch.webapp.model.Track;
import java.util.List;
import java.util.Optional;

public class TimeRepositoryImpl implements TimeRepository {

  @Override
  public Time save(Time time) {
    return null;
  }

  @Override
  public List<Time> findBestTimeForEachTrack(String username) {
    return null;
  }

  @Override
  public Optional<Time> findBestTime(Track track, String username) {
    return Optional.empty();
  }
}
