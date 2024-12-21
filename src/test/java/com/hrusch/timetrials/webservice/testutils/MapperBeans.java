package com.hrusch.timetrials.webservice.testutils;

import com.hrusch.timetrials.webservice.mapper.CombinationMapper;
import com.hrusch.timetrials.webservice.mapper.CombinationMapperImpl;
import com.hrusch.timetrials.webservice.mapper.TimeMapper;
import com.hrusch.timetrials.webservice.mapper.TimeMapperImpl;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class MapperBeans {

  @Bean
  public CombinationMapper combinationMapper() {
    return new CombinationMapperImpl();
  }

  @Bean
  public TimeMapper timeMapper(CombinationMapper combinationMapper) {
    return new TimeMapperImpl(combinationMapper);
  }
}
