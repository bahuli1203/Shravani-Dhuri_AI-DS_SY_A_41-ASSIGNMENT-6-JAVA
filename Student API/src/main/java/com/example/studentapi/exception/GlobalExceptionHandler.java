package com.example.studentapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * GLOBAL EXCEPTION HANDLER
 *
 * Intercepts exceptions thrown anywhere in the application and converts them
 * into clean, structured JSON error responses – so no stack traces are ever
 * returned to the client.
 *
 * @RestControllerAdvice = @ControllerAdvice + @ResponseBody
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    // ─────────────────────────────────────────────────────────────────────────
    // 404 – Resource Not Found
    // ─────────────────────────────────────────────────────────────────────────
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleResourceNotFound(
            ResourceNotFoundException ex) {

        return buildError(HttpStatus.NOT_FOUND, ex.getMessage(), null);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // 409 – Duplicate Email
    // ─────────────────────────────────────────────────────────────────────────
    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<Map<String, Object>> handleDuplicateEmail(
            DuplicateEmailException ex) {

        return buildError(HttpStatus.CONFLICT, ex.getMessage(), null);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // 400 – Bean Validation failures (@Valid on RequestDTO)
    // ─────────────────────────────────────────────────────────────────────────
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationErrors(
            MethodArgumentNotValidException ex) {

        // Collect field → error message pairs
        Map<String, String> fieldErrors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String field = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            fieldErrors.put(field, message);
        });

        return buildError(HttpStatus.BAD_REQUEST, "Validation failed", fieldErrors);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // 500 – Catch-all for unexpected errors
    // ─────────────────────────────────────────────────────────────────────────
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        return buildError(HttpStatus.INTERNAL_SERVER_ERROR,
                "An unexpected error occurred: " + ex.getMessage(), null);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Helper – builds a consistent error response envelope
    // ─────────────────────────────────────────────────────────────────────────
    private ResponseEntity<Map<String, Object>> buildError(
            HttpStatus status, String message, Map<String, String> errors) {

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now().toString());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", message);
        if (errors != null) {
            body.put("fieldErrors", errors);
        }
        return ResponseEntity.status(status).body(body);
    }
}
