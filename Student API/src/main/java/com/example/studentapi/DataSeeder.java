package com.example.studentapi;

import com.example.studentapi.dto.StudentRequestDTO;
import com.example.studentapi.service.StudentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * DATA SEEDER
 *
 * Runs after the application starts and inserts some demo students
 * so you can immediately test GET /api/v1/students without creating data first.
 *
 * CommandLineRunner's run() is called once, right after the Spring context is ready.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DataSeeder implements CommandLineRunner {

    private final StudentService studentService;

    @Override
    public void run(String... args) {
        log.info("Seeding demo student data…");

        studentService.createStudent(StudentRequestDTO.builder()
                .firstName("Alice").lastName("Johnson")
                .email("alice.johnson@university.edu")
                .age(20).course("Computer Science").build());

        studentService.createStudent(StudentRequestDTO.builder()
                .firstName("Bob").lastName("Smith")
                .email("bob.smith@university.edu")
                .age(22).course("Mathematics").build());

        studentService.createStudent(StudentRequestDTO.builder()
                .firstName("Carol").lastName("Williams")
                .email("carol.williams@university.edu")
                .age(21).course("Physics").build());

        log.info("✅ 3 demo students seeded successfully.");
    }
}
