package com.moe.jwttest.service;

import com.moe.jwttest.entity.User;

import java.util.List;

public interface UserService {
    List<User> findAll();
}
