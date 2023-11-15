package com.hrusch.webapp.service;

import com.hrusch.webapp.common.UserDto;
import com.hrusch.webapp.exception.UsernameAlreadyTakenException;
import com.hrusch.webapp.repository.UserEntity;
import com.hrusch.webapp.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    private final UUID uuid = UUID.randomUUID();

    @Mock
    UserRepository userRepository;
    @Mock
    BCryptPasswordEncoder encoder;

    @InjectMocks
    UserServiceImpl userService;

    @Test
    void translatingUserEntityToUserDtoWorks() {
        UserEntity entity = createEntity();

        UserDto dto = userService.createUserDto(entity);

        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.getUserId(), dto.getUserId());
        assertEquals(entity.getUsername(), dto.getUsername());
        assertEquals(entity.getEncryptedPassword(), dto.getEncryptedPassword());
    }

    @Test
    void translatingUserDtoToUserEntityWorks() {
        String encryptedPassword = "encrypted_password_123";
        when(encoder.encode(anyString())).thenReturn(encryptedPassword);
        UserDto dto = createDto();

        UserEntity entity = userService.createUserEntity(dto);

        assertEquals(dto.getId(), entity.getId());
        assertEquals(dto.getUserId(), entity.getUserId());
        assertEquals(dto.getUsername(), entity.getUsername());
        assertEquals(encryptedPassword, entity.getEncryptedPassword());
    }

    @Test
    void creatingUserReturnsUserObject() throws UsernameAlreadyTakenException {
        when(userRepository.save(any(UserEntity.class))).thenReturn(createEntity());
        var dto = createDto();

        var result = userService.createUser(dto);

        assertEquals(dto.getUserId(), result.getUserId());
        assertEquals(dto.getUsername(), result.getUsername());
    }

    @Test
    void creatingUserWithNonUniqueNameThrowsException() {
        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.of(createEntity()));
        var dto = createDto();

        var result = assertThrows(UsernameAlreadyTakenException.class, () -> userService.createUser(dto));
        assertEquals(String.format("The username has already been taken: %s", dto.getUsername()), result.getMessage());
    }

    private UserEntity createEntity() {
        return UserEntity.builder()
                .id(1L)
                .userId(uuid.toString())
                .username("Test")
                .encryptedPassword("password123")
                .build();
    }

    private UserDto createDto() {
        return UserDto.builder()
                .id(1L)
                .userId(uuid.toString())
                .username("Test")
                .password("password123")
                .encryptedPassword("")
                .build();
    }
}
