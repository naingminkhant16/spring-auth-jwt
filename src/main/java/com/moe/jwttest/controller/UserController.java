package com.moe.jwttest.controller;

import com.moe.jwttest.entity.User;
import com.moe.jwttest.payload.response.ApiResponse;
import com.moe.jwttest.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User", description = "REST APIs Endpoints for User CRUD Operation")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("")
    @Operation(summary = "Get All Users", description = "Get All Users")
    public ResponseEntity<ApiResponse<List<User>>> getALlUsers() {
        List<User> userList = userService.findAll();
        return ResponseEntity
                .ok()
                .body(
                        new ApiResponse<>(HttpStatus.OK, "success", userList)
                );
    }

    @GetMapping("/me")
    @Operation(summary = "Get Auth User", description = "Get Auth Users")
    public ResponseEntity<ApiResponse<User>> authUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User authUser = (User) authentication.getPrincipal();
        return ResponseEntity
                .ok()
                .body(new ApiResponse<>(HttpStatus.OK, "success", authUser));
    }
}
