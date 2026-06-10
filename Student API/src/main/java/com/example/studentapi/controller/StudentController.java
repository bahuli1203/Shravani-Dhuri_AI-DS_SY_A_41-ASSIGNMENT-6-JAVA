package com.example.studentapi.controller;

import com.example.studentapi.dto.StudentRequestDTO;
import com.example.studentapi.dto.StudentResponseDTO;
import com.example.studentapi.service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * LAYER 1 – CONTROLLER LAYER
 *
 * Responsibilities:
 *  - Define HTTP endpoints (URL mapping + HTTP methods)
 *  - Accept validated request bodies via @Valid + StudentRequestDTO
 *  - Delegate ALL business logic to the Service layer
 *  - Return appropriate HTTP status codes with StudentResponseDTO bodies
 *
 * The controller has ZERO business logic and ZERO knowledge of JPA entities.
 * All data flows as DTOs between this layer and the Service layer.
 *
 * Base URL: /api/v1/students
 */
@RestController
@RequestMapping("/api/v1/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    // ─────────────────────────────────────────────────────────────────────────
    // POST /api/v1/students
    // Create a new student
    // ─────────────────────────────────────────────────────────────────────────
    @PostMapping
    public ResponseEntity<StudentResponseDTO> createStudent(
            @Valid @RequestBody StudentRequestDTO requestDTO) {

        StudentResponseDTO created = studentService.createStudent(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // GET /api/v1/students
    // Retrieve all students
    // ─────────────────────────────────────────────────────────────────────────
    @GetMapping
    public ResponseEntity<List<StudentResponseDTO>> getAllStudents() {
        return ResponseEntity.ok(studentService.getAllStudents());
    }

    // ─────────────────────────────────────────────────────────────────────────
    // GET /api/v1/students/{id}
    // Retrieve a single student by ID
    // ─────────────────────────────────────────────────────────────────────────
    @GetMapping("/{id}")
    public ResponseEntity<StudentResponseDTO> getStudentById(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.getStudentById(id));
    }

    // ─────────────────────────────────────────────────────────────────────────
    // PUT /api/v1/students/{id}
    // Update an existing student (full replacement)
    // ─────────────────────────────────────────────────────────────────────────
    @PutMapping("/{id}")
    public ResponseEntity<StudentResponseDTO> updateStudent(
            @PathVariable Long id,
            @Valid @RequestBody StudentRequestDTO requestDTO) {

        StudentResponseDTO updated = studentService.updateStudent(id, requestDTO);
        return ResponseEntity.ok(updated);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // DELETE /api/v1/students/{id}
    // Delete a student by ID
    // ─────────────────────────────────────────────────────────────────────────
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();  // 204 No Content
    }
}
