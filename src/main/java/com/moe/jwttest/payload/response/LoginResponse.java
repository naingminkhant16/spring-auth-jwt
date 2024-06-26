package com.moe.jwttest.payload.response;

import com.moe.jwttest.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class LoginResponse {
    private User auth;
    private String token;
    private long expiresIn;
}
