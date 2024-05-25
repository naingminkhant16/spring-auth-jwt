package com.moe.jwttest.service;

import com.moe.jwttest.dto.LoginUserDto;
import com.moe.jwttest.dto.RegisterUserDto;
import com.moe.jwttest.entity.User;

public interface AuthenticationService {
    User register(RegisterUserDto registerUserDto);

    User authenticate(LoginUserDto loginUserDto);
}
