package com.hrusch.webapp.model.serialization;

import java.time.Duration;
import org.springframework.core.convert.converter.Converter;

public class DurationToLongConverter implements Converter<Duration, Long> {

  @Override
  public Long convert(Duration duration) {
    return duration.toMillis();
  }
}
