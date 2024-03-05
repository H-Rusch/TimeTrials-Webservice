package com.hrusch.timetrials.webservice.model.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.time.Duration;

public class CustomDurationSerializer extends JsonSerializer<Duration> {

  @Override
  public void serialize(Duration value, JsonGenerator generator, SerializerProvider serializers)
      throws IOException {
    generator.writeString(serializeDuration(value));
  }

  public static String serializeDuration(Duration duration) {
    long minutes = duration.toMinutes();
    int seconds = duration.toSecondsPart();
    int millis = duration.toMillisPart();

    return String.format("%01d:%02d.%03d", minutes, seconds, millis);
  }
}
