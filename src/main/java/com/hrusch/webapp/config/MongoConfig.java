package com.hrusch.webapp.config;

import com.hrusch.webapp.model.serialization.DurationToLongConverter;
import com.hrusch.webapp.model.serialization.LongToDurationConverter;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

@Configuration
public class MongoConfig {

  @Bean
  public MongoCustomConversions customConversions() {
    return new MongoCustomConversions(List.of(
        new DurationToLongConverter(),
        new LongToDurationConverter()));
  }
}
