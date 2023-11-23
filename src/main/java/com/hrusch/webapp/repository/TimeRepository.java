package com.hrusch.webapp.repository;

import com.hrusch.webapp.model.Track;
import com.hrusch.webapp.model.entity.TimeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TimeRepository extends JpaRepository<TimeEntity, Long> {

    Optional<TimeEntity> findFirstByTrackOrderByTimeAsc(Track track);

    Optional<TimeEntity> findFirstByTrackAndUser_IdOrderByTimeAsc(Track track, Long idOfUser);

    @Query("SELECT t FROM TimeEntity t WHERE t.time IN (SELECT MIN(ti.time) FROM TimeEntity ti WHERE ti.track = t.track)")
    List<TimeEntity> findBestTimeForEachTrack();

    @Query("SELECT t FROM TimeEntity t WHERE t.user.id = :user_id AND t.time IN "
            + "(SELECT tt FROM TimeEntity tt WHERE tt.user.id = :user_id GROUP BY tt.track HAVING tt.time = MIN(tt.time))")
    List<TimeEntity> findBestTimeForEachTrack(@Param("user_id") Long idOfUser);
}
