package com.moe.jwttest.controller;

import com.moe.jwttest.payload.request.LoginRequest;
import com.moe.jwttest.payload.request.RegisterRequest;
import com.moe.jwttest.entity.User;
import com.moe.jwttest.payload.response.ApiResponse;
import com.moe.jwttest.payload.response.LoginResponse;
import com.moe.jwttest.service.AuthenticationService;
import com.moe.jwttest.service.JwtService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final JwtService jwtService;

    public AuthenticationController(AuthenticationService authenticationService, JwtService jwtService) {
        this.authenticationService = authenticationService;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {

        User authenticatedUser = authenticationService.authenticate(loginRequest);
        String token = jwtService.generateToken(authenticatedUser);
        return ResponseEntity
                .ok()
                .body(
                        new LoginResponse(
                                authenticatedUser,
                                token,
                                jwtService.getExpirationTime()
                        )
                );
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<User>> register(@Valid @RequestBody RegisterRequest registerRequest) {

        User user = authenticationService.register(registerRequest);

        return ResponseEntity
                .ok()
                .body(
                        new ApiResponse<>(HttpStatus.OK, "Register Success.", user)
                );
    }
}
