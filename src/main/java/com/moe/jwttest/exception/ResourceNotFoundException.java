package com.moe.jwttest.exception;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String resourceName, String fieldName, String value) {
        super(resourceName + " not found with " + fieldName + " : " + value);
    }
}
