package com.example.studentapi.dto;

import lombok.*;

/**
 * DTO – DATA TRANSFER OBJECT (outbound)
 *
 * Used when the API sends data back to the client (GET / POST / PUT responses).
 * Includes the auto-generated database ID so the client can reference the resource.
 *
 * Note: No validation annotations needed here – we're building the response,
 * not accepting user input.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentResponseDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private int age;
    private String course;
}
