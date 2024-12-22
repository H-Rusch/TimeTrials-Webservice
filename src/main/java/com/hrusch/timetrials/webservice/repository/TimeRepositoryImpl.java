package com.hrusch.timetrials.webservice.repository;

import com.hrusch.timetrials.webservice.model.Time;
import com.hrusch.timetrials.webservice.model.Track;
import com.hrusch.timetrials.webservice.util.CriteriaUtil;
import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@Slf4j
@AllArgsConstructor
@Repository
public class TimeRepositoryImpl implements TimeRepository {

  private static final String COLLECTION = "times";

  private static final String TRACK = "track";
  private static final String DURATION = "duration";
  private static final String USERNAME = "username";

  private final MongoTemplate mongoTemplate;

  @Override
  public Collection<Collection<Time>> findBestTimeForEachTrack(String username) {
    Criteria usernameCriteria = CriteriaUtil.buildForValue(USERNAME, username)
        .orElse(new Criteria());

    return findBestTimeForEachTrackBasedOnCriteria(usernameCriteria)
        .entrySet()
        .stream()
        .map(it -> buildTrackAndDurationCriteria(it.getKey(), it.getValue(), usernameCriteria))
        .map(this::findTimesMatchingCriteria)
        .toList();
  }

  private Map<Track, Duration> findBestTimeForEachTrackBasedOnCriteria(Criteria criteria) {
    Aggregation aggregation = Aggregation.newAggregation(
        Aggregation.match(criteria),
        Aggregation.sort(Sort.by(Direction.ASC, DURATION)),
        Aggregation.group(TRACK)
            .first("$$ROOT").as("document"),
        Aggregation.replaceRoot("document"));

    return mongoTemplate.aggregate(aggregation, COLLECTION, Time.class)
        .getMappedResults()
        .stream()
        .collect(Collectors.toMap(
            Time::getTrack,
            Time::getDuration));
  }

  @Override
  public Collection<Time> findBestTimeForTrack(Track track, String username) {
    if (Objects.isNull(track)) {
      log.error("Track can not be null when searching best time for track.");
      return Collections.emptyList();
    }

    Criteria trackCriteria = Criteria.where(TRACK).is(track);
    Optional<Criteria> userCriteria = CriteriaUtil.buildForValue(USERNAME, username);

    Criteria combinedCriteria = userCriteria
        .map(it -> new Criteria().andOperator(trackCriteria, it))
        .orElse(trackCriteria);

    return findBestTimesByCriteria(combinedCriteria);
  }

  private Collection<Time> findBestTimesByCriteria(Criteria trackAndUsernameCriteria) {
    Optional<Duration> minimumDuration = findMinimumDurationMatchingCriteria(
        trackAndUsernameCriteria);

    if (minimumDuration.isEmpty()) {
      log.error("minimum duration is empty");
      return Collections.emptyList();
    }

    Criteria minimumDurationCriteria = Criteria.where(DURATION).is(minimumDuration.get());
    Criteria combinedCriteria = minimumDurationCriteria.andOperator(trackAndUsernameCriteria);

    return findTimesMatchingCriteria(combinedCriteria);
  }

  private Optional<Duration> findMinimumDurationMatchingCriteria(Criteria criteria) {
    Aggregation aggregation = Aggregation.newAggregation(
        Aggregation.match(criteria),
        Aggregation.sort(Sort.by(Direction.ASC, DURATION)),
        Aggregation.limit(1)
    );

    return mongoTemplate.aggregate(aggregation, COLLECTION, Time.class)
        .getMappedResults()
        .stream()
        .findFirst()
        .map(Time::getDuration);
  }

  private Collection<Time> findTimesMatchingCriteria(Criteria criteria) {
    return mongoTemplate.find(Query.query(criteria), Time.class, COLLECTION);
  }

  private Criteria buildTrackAndDurationCriteria(Track track, Duration duration, Criteria usernameCriteria) {
    return new Criteria().andOperator(
        Criteria.where(TRACK).is(track),
        Criteria.where(DURATION).is(duration),
        usernameCriteria);
  }

  @Override
  public Time saveTime(Time time) {
    return mongoTemplate.save(time, COLLECTION);
  }
}
