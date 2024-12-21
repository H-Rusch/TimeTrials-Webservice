package com.hrusch.timetrials.webservice.service;

import com.hrusch.timetrials.webservice.mapper.TimeMapper;
import com.hrusch.timetrials.webservice.model.Time;
import com.hrusch.timetrials.webservice.model.TimeDto;
import com.hrusch.timetrials.webservice.model.Track;
import com.hrusch.timetrials.webservice.repository.TimeRepository;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TimeServiceImpl implements TimeService {

  private final TimeRepository timeRepository;
  private final RecordKeeperService recordKeeperService;
  private final TimeMapper timeMapper;

  @Override
  public Collection<TimeDto> getBestTimeForEachTrack(String username) {
    return timeRepository.findBestTimeForEachTrack(username).stream()
        .map(timeMapper::map)
        .toList();
  }

  @Override
  public Optional<TimeDto> getBestTimeForTrack(Track track, String username) {
    return Optional.ofNullable(track)
        .flatMap(it -> timeRepository.findBestTimeForTrack(it, username))
        .map(timeMapper::map);
  }

  @Override
  public void saveNewTime(TimeDto timeDto) {
    Time time = timeMapper.map(timeDto, LocalDateTime.now());

    Time savedTime = timeRepository.saveTime(time);

    recordKeeperService.update(savedTime);
  }
}
