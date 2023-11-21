package com.hrusch.webapp.service;

import com.hrusch.webapp.exception.UserDoesNotExistException;
import com.hrusch.webapp.exception.UsernameAlreadyTakenException;
import com.hrusch.webapp.model.UserEntity;
import com.hrusch.webapp.model.dto.UserDto;

public interface UserService {

    UserDto createUser(UserDto userDto) throws UsernameAlreadyTakenException;

    UserEntity findUserByUserId(String userId) throws UserDoesNotExistException;
}
