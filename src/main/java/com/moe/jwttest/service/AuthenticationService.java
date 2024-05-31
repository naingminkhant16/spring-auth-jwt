package com.moe.jwttest.service;

import com.moe.jwttest.payload.request.LoginRequest;
import com.moe.jwttest.payload.request.RegisterRequest;
import com.moe.jwttest.entity.User;

public interface AuthenticationService {
    User register(RegisterRequest registerRequest);

    User authenticate(LoginRequest loginRequest);
}
