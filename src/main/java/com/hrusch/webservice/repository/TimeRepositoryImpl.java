package com.hrusch.webservice.repository;

import com.hrusch.webservice.model.Time;
import com.hrusch.webservice.model.Track;
import com.hrusch.webservice.util.CriteriaUtil;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.LimitOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ReplaceRootOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

@Slf4j
@AllArgsConstructor
@Repository
public class TimeRepositoryImpl implements TimeRepository {

  private static final String COLLECTION = "times";

  private static final String USERNAME = "username";
  private static final String TRACK = "track";

  private MongoTemplate mongoTemplate;

  @Override
  public Collection<Time> findBestTimeForEachTrack(String username) {
    Criteria criteria = CriteriaUtil.buildForValue(USERNAME, username)
        .orElse(new Criteria());

    return findBestTimeForEachTrackBasedOnCriteria(criteria);
  }

  private List<Time> findBestTimeForEachTrackBasedOnCriteria(Criteria criteria) {
    MatchOperation matchOperation = Aggregation.match(criteria);
    SortOperation sortOperation = Aggregation.sort(Sort.by(Direction.ASC, "duration"));
    GroupOperation groupOperation = Aggregation.group(TRACK)
        .first("$$ROOT").as("document");
    ReplaceRootOperation replaceRootOperation = Aggregation.replaceRoot("document");

    Aggregation aggregation = Aggregation.newAggregation(
        matchOperation,
        sortOperation,
        groupOperation,
        replaceRootOperation);

    return mongoTemplate.aggregate(aggregation, COLLECTION, Time.class)
        .getMappedResults();
  }

  @Override
  public Optional<Time> findBestTimeForTrack(Track track, String username) {
    if (Objects.isNull(track)) {
      log.error("Track can not be null when searching best time for track.");
      return Optional.empty();
    }

    Criteria trackCriteria = Criteria.where(TRACK).is(track);
    Optional<Criteria> userCriteria = CriteriaUtil.buildForValue(USERNAME, username);

    Criteria combinedCriteria = userCriteria
        .map(it -> new Criteria().andOperator(trackCriteria, it))
        .orElse(trackCriteria);

    return findBestTimeByCriteria(combinedCriteria);
  }

  private Optional<Time> findBestTimeByCriteria(Criteria criteria) {
    MatchOperation matchOperation = Aggregation.match(criteria);
    SortOperation sortOperation = Aggregation.sort(Sort.by(Direction.ASC, "duration"));
    LimitOperation limitOperation = Aggregation.limit(1);
    Aggregation aggregation = Aggregation.newAggregation(
        matchOperation,
        sortOperation,
        limitOperation);

    return mongoTemplate.aggregate(aggregation, COLLECTION, Time.class)
        .getMappedResults()
        .stream()
        .findFirst();
  }

  @Override
  public Time saveTime(Time time) {
    return mongoTemplate.save(time, COLLECTION);
  }
}
