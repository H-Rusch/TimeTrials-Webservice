package com.hrusch.webapp.service;

import com.hrusch.webapp.model.Time;
import com.hrusch.webapp.model.Track;
import java.util.Collection;
import java.util.Optional;

public interface TimeService {

  Collection<Time> getBestTimeForEachTrack(String username);

  Optional<Time> getBestTimeForTrack(Track track, String username);
}
