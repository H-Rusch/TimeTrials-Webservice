package com.hrusch.webapp.service;

import com.hrusch.webapp.exception.UserDoesNotExistException;
import com.hrusch.webapp.exception.UserIdNotFoundException;
import com.hrusch.webapp.exception.UsernameAlreadyTakenException;
import com.hrusch.webapp.exception.UsernameNotFoundException;
import com.hrusch.webapp.model.dto.UserDto;
import com.hrusch.webapp.model.entity.UserEntity;
import com.hrusch.webapp.repository.UserRepository;
import org.modelmapper.ModelMapper;
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
    private final ModelMapper modelMapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
    }

    @Override
    public UserDto createUser(UserDto userDto) throws UsernameAlreadyTakenException {
        UserEntity userEntity = convertToUserEntity(userDto);

        var foundUser = userRepository.findByUsername(userEntity.getUsername());
        if (foundUser.isPresent()) {
            LOG.warn("Trying to create use with a name that has already been taken: {}", userEntity.getUsername());
            throw new UsernameAlreadyTakenException(userEntity.getUsername());
        }

        UserEntity createdUser = userRepository.save(userEntity);
        LOG.info("Created user with user-id: {}", createdUser.getUserId());

        return convertToUserDto(createdUser);
    }

    @Override
    public UserEntity findUserByUserId(String userId) throws UserDoesNotExistException {
        var entity = userRepository.findByUserId(userId);

        if (entity.isEmpty()) {
            LOG.warn("Trying to find user with userId {} failed, as no user with this userId exists", userId);
            throw new UserIdNotFoundException(userId);
        }

        return entity.get();
    }

    @Override
    public UserEntity findUserByUsername(String username) throws UserDoesNotExistException {
        var entity = userRepository.findByUsername(username);

        if (entity.isEmpty()) {
            LOG.warn("Trying to find user with username '{}' failed, as no user with this username exists", username);
            throw new UsernameNotFoundException(username);
        }

        return entity.get();
    }

    private String encryptPassword(String password) {
        return passwordEncoder.encode(password);
    }

    UserEntity convertToUserEntity(UserDto userDto) {
        var encryptedPassword = encryptPassword(userDto.getPassword());
        UserEntity userEntity = modelMapper.map(userDto, UserEntity.class);
        userEntity.setEncryptedPassword(encryptedPassword);

        return userEntity;
    }

    UserDto convertToUserDto(UserEntity userEntity) {
        return modelMapper.map(userEntity, UserDto.class);
    }
}
