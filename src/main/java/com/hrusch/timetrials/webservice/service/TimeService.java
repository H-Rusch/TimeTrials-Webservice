package com.hrusch.timetrials.webservice.service;

import com.hrusch.timetrials.webservice.model.TimeDto;
import com.hrusch.timetrials.webservice.model.Track;
import java.util.Collection;
import java.util.List;

public interface TimeService {

  List<List<TimeDto>> getBestTimeForEachTrack(String username);

  Collection<TimeDto> getBestTimeForTrack(Track track, String username);

  void saveNewTime(TimeDto timeDto);
}
