package com.hrusch.webservice.util;


import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.data.mongodb.core.query.Criteria;

class CriteriaUtilTest {

  @Nested
  class CriteriaUtilCreateForValueTest {

    @Test
    void givenNullValue_whenCreateForValue_thenReturnEmpty() {
      // given, when & then
      assertThat(CriteriaUtil.buildForValue("key", null))
          .isEmpty();
    }

    @Test
    void givenValue_whenCreateForValue_thenReturnValidCriterion() {
      // given
      String key = "key";
      String value = "value";
      Criteria expectedCriteria = Criteria.where(key).is(value);

      // when
      Optional<Criteria> result = CriteriaUtil.buildForValue(key, value);

      // then
      assertThat(result)
          .isPresent()
          .hasValue(expectedCriteria);
    }
  }
}