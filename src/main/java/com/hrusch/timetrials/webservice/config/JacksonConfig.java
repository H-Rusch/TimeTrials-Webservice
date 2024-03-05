package com.hrusch.timetrials.webservice.config;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.hrusch.timetrials.webservice.model.serialization.CustomDurationDeserializer;
import com.hrusch.timetrials.webservice.model.serialization.CustomDurationSerializer;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {

  private static final String DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

  @Bean
  public ObjectMapper objectMapper() {
    return JsonMapper.builder()
        .addModule(new JavaTimeModule())
        .addModule(createDurationModule())
        .addModule(createLocalDateTimeModule())
        .configure(SerializationFeature.INDENT_OUTPUT, true)
        .build();
  }

  private static Module createDurationModule() {
    SimpleModule durationModule = new SimpleModule();
    durationModule.addSerializer(Duration.class, new CustomDurationSerializer());
    durationModule.addDeserializer(Duration.class, new CustomDurationDeserializer());

    return durationModule;
  }

  private static Module createLocalDateTimeModule() {
    SimpleModule dateTimeModule = new SimpleModule();
    dateTimeModule.addSerializer(LocalDateTime.class,
        new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DATETIME_PATTERN)));
    dateTimeModule.addDeserializer(LocalDateTime.class,
        new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(DATETIME_PATTERN)));

    return dateTimeModule;
  }
}
