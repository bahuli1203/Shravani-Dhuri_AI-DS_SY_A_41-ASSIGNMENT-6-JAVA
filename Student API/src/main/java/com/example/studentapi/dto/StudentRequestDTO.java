package com.example.studentapi.dto;

import jakarta.validation.constraints.*;
import lombok.*;

/**
 * DTO – DATA TRANSFER OBJECT (inbound)
 *
 * Used when a client sends data to the API (POST / PUT requests).
 * Bean Validation annotations enforce input correctness before
 * the request even reaches the Service layer.
 *
 * Why a separate DTO?
 *  - The entity may contain sensitive/internal fields (audit timestamps, etc.)
 *    that must never be exposed or accepted from outside.
 *  - DTOs create a clean contract between the API surface and the domain model.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentRequestDTO {

    @NotBlank(message = "First name is required")
    @Size(max = 100, message = "First name must be at most 100 characters")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(max = 100, message = "Last name must be at most 100 characters")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be a valid email address")
    @Size(max = 150, message = "Email must be at most 150 characters")
    private String email;

    @Min(value = 1, message = "Age must be at least 1")
    @Max(value = 120, message = "Age must be at most 120")
    private int age;

    @NotBlank(message = "Course is required")
    @Size(max = 100, message = "Course name must be at most 100 characters")
    private String course;
}
