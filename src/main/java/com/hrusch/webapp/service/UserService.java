package com.hrusch.webapp.service;

import com.hrusch.webapp.error.exception.UserDoesNotExistException;
import com.hrusch.webapp.error.exception.UsernameAlreadyTakenException;
import com.hrusch.webapp.model.dto.UserDto;
import com.hrusch.webapp.model.entity.UserEntity;

public interface UserService {

    UserDto createUser(UserDto userDto) throws UsernameAlreadyTakenException;

    UserEntity findUserByUserId(String userId) throws UserDoesNotExistException;

    UserEntity findUserByUsername(String username) throws UserDoesNotExistException;
}
