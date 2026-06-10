package com.example.studentapi.service.impl;

import com.example.studentapi.dto.StudentRequestDTO;
import com.example.studentapi.dto.StudentResponseDTO;
import com.example.studentapi.entity.Student;
import com.example.studentapi.exception.DuplicateEmailException;
import com.example.studentapi.exception.ResourceNotFoundException;
import com.example.studentapi.repository.StudentRepository;
import com.example.studentapi.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * LAYER 2 – SERVICE LAYER (implementation)
 *
 * Contains ALL business logic:
 *   - DTO ↔ Entity mapping (no extra library needed; manual mapping keeps things explicit)
 *   - Duplicate-email guard
 *   - Delegates persistence to the Repository layer
 *
 * @Transactional ensures DB operations are rolled back on any unchecked exception.
 */
@Service
@RequiredArgsConstructor  // Lombok: injects final fields via constructor (best practice over @Autowired)
@Transactional
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

    // ─────────────────────────────────────────────────────────────────────────
    // CREATE
    // ─────────────────────────────────────────────────────────────────────────

    @Override
    public StudentResponseDTO createStudent(StudentRequestDTO requestDTO) {
        // Business rule: email must be unique
        if (studentRepository.existsByEmail(requestDTO.getEmail())) {
            throw new DuplicateEmailException(
                    "A student with email '" + requestDTO.getEmail() + "' already exists.");
        }

        Student student = mapToEntity(requestDTO);
        Student savedStudent = studentRepository.save(student);
        return mapToResponseDTO(savedStudent);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // READ ALL
    // ─────────────────────────────────────────────────────────────────────────

    @Override
    @Transactional(readOnly = true)
    public List<StudentResponseDTO> getAllStudents() {
        return studentRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    // ─────────────────────────────────────────────────────────────────────────
    // READ ONE
    // ─────────────────────────────────────────────────────────────────────────

    @Override
    @Transactional(readOnly = true)
    public StudentResponseDTO getStudentById(Long id) {
        Student student = findStudentOrThrow(id);
        return mapToResponseDTO(student);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // UPDATE
    // ─────────────────────────────────────────────────────────────────────────

    @Override
    public StudentResponseDTO updateStudent(Long id, StudentRequestDTO requestDTO) {
        Student existing = findStudentOrThrow(id);

        // If the email is being changed, ensure the new one is not taken by another student
        if (!existing.getEmail().equalsIgnoreCase(requestDTO.getEmail())
                && studentRepository.existsByEmail(requestDTO.getEmail())) {
            throw new DuplicateEmailException(
                    "Email '" + requestDTO.getEmail() + "' is already in use by another student.");
        }

        // Update fields in-place (entity is already managed by JPA context)
        existing.setFirstName(requestDTO.getFirstName());
        existing.setLastName(requestDTO.getLastName());
        existing.setEmail(requestDTO.getEmail());
        existing.setAge(requestDTO.getAge());
        existing.setCourse(requestDTO.getCourse());

        Student updatedStudent = studentRepository.save(existing);
        return mapToResponseDTO(updatedStudent);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // DELETE
    // ─────────────────────────────────────────────────────────────────────────

    @Override
    public void deleteStudent(Long id) {
        if (!studentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Student not found with id: " + id);
        }
        studentRepository.deleteById(id);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // PRIVATE HELPER METHODS
    // ─────────────────────────────────────────────────────────────────────────

    /** Finds a student by ID or throws a descriptive 404 exception. */
    private Student findStudentOrThrow(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Student not found with id: " + id));
    }

    /**
     * Maps a RequestDTO → Entity.
     * The ID is intentionally NOT set here; the DB generates it on insert.
     */
    private Student mapToEntity(StudentRequestDTO dto) {
        return Student.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .age(dto.getAge())
                .course(dto.getCourse())
                .build();
    }

    /** Maps an Entity → ResponseDTO (includes the DB-generated ID). */
    private StudentResponseDTO mapToResponseDTO(Student student) {
        return StudentResponseDTO.builder()
                .id(student.getId())
                .firstName(student.getFirstName())
                .lastName(student.getLastName())
                .email(student.getEmail())
                .age(student.getAge())
                .course(student.getCourse())
                .build();
    }
}
