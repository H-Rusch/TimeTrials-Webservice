package com.hrusch.webapp.repository;

import com.hrusch.webapp.model.Time;
import com.hrusch.webapp.model.Track;
import java.util.Collection;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface TimeRepository {

    Collection<Time> findBestTimeForEachTrack(String username);

    Optional<Time> findBestTimeForTrack(Track track, String username);

    Time saveTime(Time time);
}
