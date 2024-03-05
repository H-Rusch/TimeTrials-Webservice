package com.hrusch.webservice.model.serialization;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hrusch.webservice.model.Track;
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
