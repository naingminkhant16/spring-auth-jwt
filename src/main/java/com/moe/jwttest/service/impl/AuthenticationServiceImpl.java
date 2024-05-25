package com.moe.jwttest.service.impl;

import com.moe.jwttest.dto.LoginUserDto;
import com.moe.jwttest.dto.RegisterUserDto;
import com.moe.jwttest.entity.Role;
import com.moe.jwttest.entity.User;
import com.moe.jwttest.repository.RoleRepository;
import com.moe.jwttest.repository.UserRepository;
import com.moe.jwttest.service.AuthenticationService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final BCryptPasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public AuthenticationServiceImpl(UserRepository userRepository, AuthenticationManager authenticationManager, BCryptPasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    @Override
    public User register(RegisterUserDto registerUserDto) {
        //find provided role id
        Role role = roleRepository.findById(registerUserDto.getRole_id()).orElseThrow();

        User user = new User();

        user.setName(registerUserDto.getName());
        user.setEmail(registerUserDto.getEmail());
        user.setPassword(
                passwordEncoder.encode(registerUserDto.getPassword())
        );
        user.setRoles(List.of(role));

        return userRepository.save(user);
    }

    @Override
    public User authenticate(LoginUserDto loginUserDto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginUserDto.getEmail(),
                        loginUserDto.getPassword()
                )
        );

        return userRepository.findByEmail(loginUserDto.getEmail()).orElseThrow();
    }
}
