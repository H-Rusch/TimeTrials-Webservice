package com.hrusch.timetrials.webservice.service;

import com.hrusch.timetrials.webservice.model.Time;
import com.hrusch.timetrials.webservice.model.TimeDto;
import com.hrusch.timetrials.webservice.model.Track;
import java.util.Collection;
import java.util.Optional;

public interface TimeService {

  Collection<Time> getBestTimeForEachTrack(String username);

  Optional<Time> getBestTimeForTrack(Track track, String username);

  void saveNewTime(TimeDto timeDto);
}
