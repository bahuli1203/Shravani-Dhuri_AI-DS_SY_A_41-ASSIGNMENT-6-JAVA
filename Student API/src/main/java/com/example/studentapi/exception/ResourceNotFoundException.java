package com.example.studentapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Thrown by the Service layer when a requested resource does not exist.
 * The @ResponseStatus annotation maps this to HTTP 404 by default,
 * but the GlobalExceptionHandler gives us finer control over the response body.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
