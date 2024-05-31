package com.moe.jwttest.service.impl;

import com.moe.jwttest.payload.request.LoginRequest;
import com.moe.jwttest.payload.request.RegisterRequest;
import com.moe.jwttest.entity.Role;
import com.moe.jwttest.entity.User;
import com.moe.jwttest.exception.ResourceNotFoundException;
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
    public User register(RegisterRequest registerRequest) {
        //find provided role id
        Role role = roleRepository
                .findById(registerRequest.getRole_id())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Role", "id",
                                registerRequest.getRole_id().toString()
                        )
                );

        User user = new User();

        user.setName(registerRequest.getName());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(
                passwordEncoder.encode(registerRequest.getPassword())
        );
        user.setRoles(List.of(role));

        return userRepository.save(user);
    }

    @Override
    public User authenticate(LoginRequest loginRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        return userRepository.findByEmail(loginRequest.getEmail()).orElseThrow();
    }
}
