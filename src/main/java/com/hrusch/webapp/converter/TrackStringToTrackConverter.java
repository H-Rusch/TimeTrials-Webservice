package com.hrusch.webapp.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hrusch.webapp.model.Track;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class TrackStringToTrackConverter implements Converter<String, Track> {

    @Override
    public Track convert(String source) {
        try {
            return Track.forValue(source);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
