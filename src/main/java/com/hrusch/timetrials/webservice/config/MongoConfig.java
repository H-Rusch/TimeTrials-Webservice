package com.hrusch.timetrials.webservice.config;

import com.hrusch.timetrials.webservice.model.serialization.DurationToLongConverter;
import com.hrusch.timetrials.webservice.model.serialization.LongToDurationConverter;
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
