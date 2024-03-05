package com.hrusch.webservice.repository;

import com.hrusch.webservice.model.Time;
import com.hrusch.webservice.model.Track;
import java.util.Collection;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface TimeRepository {

    Collection<Time> findBestTimeForEachTrack(String username);

    Optional<Time> findBestTimeForTrack(Track track, String username);

    Time saveTime(Time time);
}
