package com.hrusch.webapp.model.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.Duration;

public class CustomDurationSerializer extends JsonSerializer<Duration> {

    @Override
    public void serialize(Duration value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        long minutes = value.toMinutes();
        int seconds = value.toSecondsPart();
        int millis = value.toMillisPart();
        String durationString = String.format("%01d:%02d.%03d", minutes, seconds, millis);

        gen.writeString(durationString);
    }
}
