package com.hrusch.timetrials.webservice.model.serialization;

import java.time.Duration;
import org.springframework.core.convert.converter.Converter;

public class LongToDurationConverter implements Converter<Long, Duration> {

  @Override
  public Duration convert(Long millis) {
    return Duration.ofMillis(millis);
  }
}
