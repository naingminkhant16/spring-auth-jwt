package com.moe.jwttest.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ApiResponse<T> {
    private HttpStatus status;
    private String message;
    private T data;
}
