package com.moe.jwttest.payload.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class RegisterRequest {
    @NotNull(message = "Name is required.")
    @Size(min = 1, message = "Name is required.")
    private String name;

    @NotNull(message = "Email is required.")
    @Size(min = 1, message = "Email is required.")
    @Email
    private String email;

    @NotNull(message = "Password is required.")
    @Size(min = 6, message = "Password must have at least 6 chars.")
    private String password;

    @NotNull(message = "Role Id is required.")
    @Positive
    private Long role_id;
}
