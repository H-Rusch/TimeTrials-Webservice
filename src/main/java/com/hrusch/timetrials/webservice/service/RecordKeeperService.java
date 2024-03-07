package com.hrusch.timetrials.webservice.service;

import com.hrusch.timetrials.webservice.model.Time;
import com.hrusch.timetrials.webservice.model.Track;
import com.hrusch.timetrials.webservice.repository.TimeRepository;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RecordKeeperService {

  private final PublishNewRecordKafkaProducerService publishNewRecordKafkaProducerService;

  private final Map<Track, Time> recordForTrackMap;

  @Autowired
  public RecordKeeperService(TimeRepository timeRepository,
      PublishNewRecordKafkaProducerService publishNewRecordKafkaProducerService) {
    this.publishNewRecordKafkaProducerService = publishNewRecordKafkaProducerService;
    this.recordForTrackMap = buildRecordForTrackMap(timeRepository.findBestTimeForEachTrack(null));
  }

  public void update(Time time) {
    if (Objects.isNull(time) || Objects.isNull(time.getTrack())) {
      log.error("Cannot process time object due to errors: {}", time);
      return;
    }

    Track track = time.getTrack();
    if (!recordForTrackMap.containsKey(track) ||
        time.getDuration().compareTo(recordForTrackMap.get(track).getDuration()) <= 0) {
      log.info("Updating the record for track {} with time {}", track, time);
      recordForTrackMap.put(time.getTrack(), time);

      publishNewRecordKafkaProducerService.publish(time);
    } else {
      log.trace("Incoming time for track {} was no new record.", track);
    }
  }

  private static Map<Track, Time> buildRecordForTrackMap(Collection<Time> records) {
    return records.stream()
        .collect(Collectors.toMap(
            Time::getTrack,
            Function.identity()));
  }
}
