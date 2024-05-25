package com.moe.jwttest.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class LoginUserDto {
    @NotNull(message = "Email is required.")
    @Size(min = 1, message = "Email is required.")
    private String email;

    @NotNull(message = "Password is required.")
    @Size(min = 1, message = "Password is required.")
    private String password;
}
