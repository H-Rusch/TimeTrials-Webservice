package com.hrusch.webservice.service;

import com.hrusch.webservice.model.Time;
import com.hrusch.webservice.model.TimeDto;
import com.hrusch.webservice.model.Track;
import java.util.Collection;
import java.util.Optional;

public interface TimeService {

  Collection<Time> getBestTimeForEachTrack(String username);

  Optional<Time> getBestTimeForTrack(Track track, String username);

  void saveNewTime(TimeDto timeDto);
}
