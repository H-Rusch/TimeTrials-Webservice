package com.hrusch.webapp.service;

import com.hrusch.webapp.model.Time;
import com.hrusch.webapp.model.Track;

import java.util.List;
import java.util.Optional;

public interface TimeService {

    Time saveTime(Time timeDto);

    List<Time> getBestTimes(String username);

    Optional<Time> getBestTimeForTrack(Track track, String username);
}
