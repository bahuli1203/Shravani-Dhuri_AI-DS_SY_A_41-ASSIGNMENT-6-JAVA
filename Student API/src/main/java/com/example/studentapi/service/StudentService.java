package com.example.studentapi.service;

import com.example.studentapi.dto.StudentRequestDTO;
import com.example.studentapi.dto.StudentResponseDTO;

import java.util.List;

/**
 * LAYER 2 – SERVICE LAYER (interface)
 *
 * Defines the business contract. The Controller depends ONLY on this interface,
 * never on the concrete implementation – enabling easy unit testing via mocks.
 *
 * All methods accept/return DTOs so no JPA entities leak into the Controller.
 */
public interface StudentService {

    /** Create a new student. Returns the saved student with its generated ID. */
    StudentResponseDTO createStudent(StudentRequestDTO requestDTO);

    /** Retrieve all students. */
    List<StudentResponseDTO> getAllStudents();

    /** Retrieve a single student by ID. Throws ResourceNotFoundException if absent. */
    StudentResponseDTO getStudentById(Long id);

    /** Update an existing student. Throws ResourceNotFoundException if absent. */
    StudentResponseDTO updateStudent(Long id, StudentRequestDTO requestDTO);

    /** Delete a student by ID. Throws ResourceNotFoundException if absent. */
    void deleteStudent(Long id);
}
