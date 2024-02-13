package com.hrusch.webapp.service;

import com.hrusch.webapp.model.Track;
import com.hrusch.webapp.repository.TimeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TimeServiceImplTest {

    @Mock
    TimeRepository timeRepository;

    @InjectMocks
    TimeServiceImpl timeService;

    @Test
    void saveTime_whenGivenValidTimeRequestAndUserPresent_createAndReturnTimeObject() throws Exception {
/*        var userEntity = createEntity(userId);
        var timeEntity = createTimeEntity(userEntity);
        when(modelMapper.map(any(TimeDto.class), eq(TimeEntity.class)))
                .thenReturn(timeEntity);
        when(userService.findUserByUserId(any(String.class))).thenReturn(userEntity);
        when(timeRepository.save(any(TimeEntity.class))).thenReturn(timeEntity);
        when(modelMapper.map(any(TimeEntity.class), eq(TimeDto.class)))
                .thenReturn(new ModelMapper().map(timeEntity, TimeDto.class));
        var timeDto = createTimeDto(userId);

        var resultingTimeDto = timeService.saveTime(timeDto);

        assertThat(resultingTimeDto.getTime()).isEqualTo(timeDto.getTime());
        assertThat(resultingTimeDto.getUserId()).isEqualTo(timeDto.getUserId());
        assertThat(resultingTimeDto.getCreatedAt()).isEqualTo(timeDto.getCreatedAt());
        assertThat(resultingTimeDto.getUsername()).isNotNull();*/
    }

    @Test
    void saveTime_whenGivenValidTimeRequestButUserNotPresent_throwsException() throws Exception {
       /* when(userService.findUserByUserId(any(String.class)))
                .thenThrow(new UserIdNotFoundException(userId.toString()));
        var timeDto = createTimeDto(userId);

        var exception = assertThrows(UserDoesNotExistException.class, () -> timeService.saveTime(timeDto));
        assertThat(exception.getMessage())
        .isEqualTo("User with the userId '%s' does not exist.", userId.toString());*/
    }

    @Test
    void getBestTimes_whenCalledWithoutUsername_callCorrectMethod() {

        /*timeService.getBestTimes();

        verify(timeRepository, times(1)).findBestTimeForEachTrack();*/
    }

    @Test
    void getBestTimes_whenGivenUsernameForExistingUser_callCorrectMethod() throws Exception {
        /*when(userService.findUserByUsername(any(String.class)))
                .thenReturn(createEntity(UUID.randomUUID()));

        timeService.getBestTimes("username");

        verify(timeRepository, times(1)).findBestTimeForEachTrack(any(Long.class));*/
    }
/*
    @Test
    void getBestTimes_whenGivenUsernameForNonExistingUser_throwException() throws Exception {
        when(userService.findUserByUsername(any(String.class)))
                .thenThrow(new UsernameNotFoundException(""));

        assertThrows(UserDoesNotExistException.class, () -> timeService.getBestTimes("username"));
    }

    @Test
    void getBestTimeForTrack_whenCalledWithoutUsername_callCorrectMethod() {
        var track = Track.WATER_PARK;

        timeService.getBestTimeForTrack(track);

        verify(timeRepository, times(1)).findFirstByTrackOrderByTimeAsc(any(Track.class));
    }

    @Test
    void getBestTimeForTrack_whenGivenUsernameForExistingUser_callCorrectMethod() throws Exception {
        var track = Track.WATER_PARK;
        when(userService.findUserByUsername(any(String.class)))
                .thenReturn(createEntity(UUID.randomUUID()));

        timeService.getBestTimeForTrack(track, "username");

        verify(timeRepository, times(1)).findFirstByTrackAndUser_IdOrderByTimeAsc(any(Track.class), any(Long.class));
    }

    @Test
    void getBestTimeForTrack_whenGivenUsernameForNonExistingUser_throwException() throws Exception {
        var track = Track.WATER_PARK;
        when(userService.findUserByUsername(any(String.class)))
                .thenThrow(new UsernameNotFoundException(""));

        assertThrows(UserDoesNotExistException.class, () -> timeService.getBestTimeForTrack(track, "username"));
    }*/
}
