package com.hrusch.webapp.service;

import com.hrusch.webapp.common.UserDto;
import com.hrusch.webapp.repository.UserEntity;
import com.hrusch.webapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        UserEntity userEntity = createUserEntity(userDto);

        UserEntity createdUser = userRepository.save(userEntity);
        System.out.println(createdUser);

        return createUserDto(createdUser);
    }

    public UserEntity createUserEntity(UserDto userDto) {
        var encryptedPassword = encryptPassword(userDto.getPassword());

        return UserEntity.builder()
                .userId(userDto.getUserId())
                .username(userDto.getUsername())
                .encryptedPassword(encryptedPassword)
                .build();
    }

    private String encryptPassword(String password) {
        return passwordEncoder.encode(password);
    }

    public UserDto createUserDto(UserEntity entity) {
        return UserDto.builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .username(entity.getUsername())
                .password("")
                .encryptedPassword(entity.getEncryptedPassword())
                .build();
    }
}
