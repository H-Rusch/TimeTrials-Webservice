package com.hrusch.webapp.service;

import com.hrusch.webapp.common.UserDto;
import com.hrusch.webapp.exception.UsernameAlreadyTakenException;
import com.hrusch.webapp.repository.UserEntity;
import com.hrusch.webapp.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDto createUser(UserDto userDto) throws UsernameAlreadyTakenException {
        UserEntity userEntity = createUserEntity(userDto);

        var foundUser = userRepository.findByUsername(userEntity.getUsername());
        if (foundUser.isPresent()) {
            LOG.warn("Trying to create use with a name that has already been taken: {}", userEntity.getUsername());
            throw new UsernameAlreadyTakenException(userEntity.getUsername());
        }

        UserEntity createdUser = userRepository.save(userEntity);
        LOG.info("Created user with user-id: {}", createdUser.getUserId());

        return createUserDto(createdUser);
    }

    public UserEntity createUserEntity(UserDto userDto) {
        var encryptedPassword = encryptPassword(userDto.getPassword());

        return UserEntity.builder()
                .id(userDto.getId())
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
