package com.moe.jwttest.service.impl;

import com.moe.jwttest.entity.User;
import com.moe.jwttest.repository.UserRepository;
import com.moe.jwttest.service.UserService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Cacheable(value = "userCache")
    public List<User> findAll() {
        return userRepository.findAll();
    }
}
