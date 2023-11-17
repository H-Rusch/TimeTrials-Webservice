package com.hrusch.webapp.service;

import com.hrusch.webapp.exception.UserDoesNotExistException;
import com.hrusch.webapp.repository.TimeEntity;
import com.hrusch.webapp.repository.TimeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static com.hrusch.webapp.TimeUtil.createTimeDto;
import static com.hrusch.webapp.TimeUtil.createTimeEntity;
import static com.hrusch.webapp.UserUtil.createEntity;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TimeServiceImplTest {

    private final UUID userId = UUID.randomUUID();

    @Mock
    TimeRepository timeRepository;
    @Mock
    UserService userService;

    @InjectMocks
    TimeServiceImpl timeService;

    @Test
    void saveTime_whenGivenValidTimeRequestAndUserPresent_createAndReturnTimeObject() throws Exception {
        var user = createEntity(userId);
        when(userService.findUserByUserId(any(String.class))).thenReturn(user);
        when(timeRepository.save(any(TimeEntity.class))).thenReturn(createTimeEntity(user));
        var timeDto = createTimeDto(userId);

        var resultingTimeDto = timeService.saveTime(timeDto);

        assertThat(resultingTimeDto.getTime()).isEqualTo(timeDto.getTime());
        assertThat(resultingTimeDto.getUserId()).isEqualTo(timeDto.getUserId());
        assertThat(resultingTimeDto.getCreatedAt()).isEqualTo(timeDto.getCreatedAt());
        assertThat(resultingTimeDto.getUsername()).isNotNull();
    }

    @Test
    void saveTime_whenGivenValidTimeRequestButUserNotPresent_throwsException() throws Exception {
        when(userService.findUserByUserId(any(String.class)))
                .thenThrow(new UserDoesNotExistException(userId.toString()));
        var timeDto = createTimeDto(userId);

        var exception = assertThrows(UserDoesNotExistException.class, () -> timeService.saveTime(timeDto));
        assertThat(exception.getMessage()).isEqualTo("User with the userId %s does not exist.", userId.toString());
    }

}
