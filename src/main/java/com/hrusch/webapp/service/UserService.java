package com.hrusch.webapp.service;

import com.hrusch.webapp.common.UserDto;
import com.hrusch.webapp.exception.UsernameAlreadyTakenException;

public interface UserService {

    UserDto createUser(UserDto userDto) throws UsernameAlreadyTakenException;
}
