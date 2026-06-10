package com.example.studentapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point for the Student API Spring Boot application.
 *
 * Architecture:
 *   Controller  →  Service  →  Repository  →  H2 (in-memory DB)
 *   All inter-layer data exchange uses DTOs (StudentRequestDTO / StudentResponseDTO)
 */
@SpringBootApplication
public class StudentApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(StudentApiApplication.class, args);
    }
}
