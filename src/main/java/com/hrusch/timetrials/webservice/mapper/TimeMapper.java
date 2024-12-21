package com.hrusch.timetrials.webservice.mapper;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

import com.hrusch.openapi.model.MkApiTimeRequest;
import com.hrusch.openapi.model.MkApiTimeResponse;
import com.hrusch.timetrials.webservice.model.Time;
import com.hrusch.timetrials.webservice.model.TimeDto;
import com.hrusch.timetrials.webservice.model.Track;
import com.hrusch.timetrials.webservice.model.serialization.CustomDurationDeserializer;
import com.hrusch.timetrials.webservice.model.serialization.CustomDurationSerializer;
import java.time.Duration;
import java.time.LocalDateTime;
import lombok.SneakyThrows;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = SPRING,
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    uses = {CombinationMapper.class},
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TimeMapper {

  @Mapping(source = "username", target = "username")
  @Mapping(source = "track", target = "track")
  @Mapping(source = "duration", target = "duration")
  @Mapping(source = "combination", target = "combination",
      nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  TimeDto map(MkApiTimeRequest timeApiTimeRequest);

  @Mapping(source = "username", target = "username")
  @Mapping(source = "track", target = "track")
  @Mapping(source = "duration", target = "duration")
  @Mapping(source = "combination", target = "combination",
      nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  @Mapping(source = "createdAt", target = "createdAt")
  MkApiTimeResponse map(TimeDto timeDto);

  @Mapping(source = "timeDto.username", target = "username")
  @Mapping(source = "timeDto.track", target = "track")
  @Mapping(source = "timeDto.duration", target = "duration")
  @Mapping(source = "timeDto.combination", target = "combination")
  @Mapping(source = "timestamp", target = "createdAt")
  Time map(TimeDto timeDto, LocalDateTime timestamp);

  TimeDto map(Time time);

  @SneakyThrows
  default Track mapToTrack(String trackName) {
    return Track.forValue(trackName);
  }

  default String mapTrackToString(Track track) {
    return track.getName();
  }

  @SneakyThrows
  default Duration mapToDuration(String durationString) {
    return CustomDurationDeserializer.deserializeDuration(durationString);
  }

  default String mapDurationToString(Duration duration) {
    return CustomDurationSerializer.serializeDuration(duration);
  }
}
