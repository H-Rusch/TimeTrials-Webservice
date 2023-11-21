package com.hrusch.webapp.model.serialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.hrusch.webapp.exception.DurationDeserializationException;

import java.io.IOException;
import java.time.Duration;
import java.util.regex.Pattern;

public class CustomDurationDeserializer extends JsonDeserializer<Duration> {

    private static final Pattern DURATION_PATTERN = Pattern.compile("^(\\d+):(\\d{2})\\.(\\d{3})$");

    @Override
    public Duration deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        var durationString = jsonParser.getValueAsString();

        var matcher = DURATION_PATTERN.matcher(durationString);
        if (!matcher.matches()) {
            throw new DurationDeserializationException("Duration does not match specified format.");
        }

        // build duration string into ISO format
        String isoDurationString = String.format("PT%sM%s.%sS", matcher.group(1), matcher.group(2), matcher.group(3));
        return Duration.parse(isoDurationString);
    }
}
