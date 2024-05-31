package com.moe.jwttest.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResourceResponse<T> {
    private int pageNo;
    private int limit;
    private HttpStatus status;
    private String message;
    private T data;
}
