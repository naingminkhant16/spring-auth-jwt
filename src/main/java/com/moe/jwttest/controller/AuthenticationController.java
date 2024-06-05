package com.moe.jwttest.controller;

import com.moe.jwttest.payload.request.LoginRequest;
import com.moe.jwttest.payload.request.RegisterRequest;
import com.moe.jwttest.entity.User;
import com.moe.jwttest.payload.response.ApiResponse;
import com.moe.jwttest.payload.response.LoginResponse;
import com.moe.jwttest.service.AuthenticationService;
import com.moe.jwttest.service.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Endpoints for authentication")
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final JwtService jwtService;

    public AuthenticationController(AuthenticationService authenticationService, JwtService jwtService) {
        this.authenticationService = authenticationService;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    @Operation(summary = "Login Attempt", description = "User/Admin Login")
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
    @Operation(summary = "User Registration", description = "User/Admin Registration")
    public ResponseEntity<ApiResponse<User>> register(@Valid @RequestBody RegisterRequest registerRequest) {

        User user = authenticationService.register(registerRequest);

        return ResponseEntity
                .ok()
                .body(
                        new ApiResponse<>(HttpStatus.OK, "Register Success.", user)
                );
    }
}
