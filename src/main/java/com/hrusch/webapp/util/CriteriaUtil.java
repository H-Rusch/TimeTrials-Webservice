package com.hrusch.webapp.util;

import java.util.Optional;
import org.springframework.data.mongodb.core.query.Criteria;

public class CriteriaUtil {

  private CriteriaUtil() {
  }

  public static <T> Optional<Criteria> buildForValue(String key, T value) {
    return Optional.ofNullable(value)
        .map(it -> Criteria.where(key).is(value));
  }

}
