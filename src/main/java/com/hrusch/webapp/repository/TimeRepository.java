package com.hrusch.webapp.repository;

import com.hrusch.webapp.model.Time;
import com.hrusch.webapp.model.Track;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TimeRepository {

    Time save(Time time);

    List<Time> findBestTimeForEachTrack(String username);

    Optional<Time> findBestTime(Track track, String username);
}
