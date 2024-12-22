package com.hrusch.timetrials.webservice.repository;

import com.hrusch.timetrials.webservice.model.Time;
import com.hrusch.timetrials.webservice.model.Track;
import java.util.Collection;
import org.springframework.stereotype.Repository;

@Repository
public interface TimeRepository {

  Collection<Collection<Time>> findBestTimeForEachTrack(String username);

  Collection<Time> findBestTimeForTrack(Track track, String username);

  Time saveTime(Time time);
}
