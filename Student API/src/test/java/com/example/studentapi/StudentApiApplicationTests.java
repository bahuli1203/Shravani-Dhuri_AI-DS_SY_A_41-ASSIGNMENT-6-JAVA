package com.example.studentapi;

import com.example.studentapi.dto.StudentRequestDTO;
import com.example.studentapi.dto.StudentResponseDTO;
import com.example.studentapi.exception.ResourceNotFoundException;
import com.example.studentapi.repository.StudentRepository;
import com.example.studentapi.service.StudentService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

/**
 * Integration tests for StudentService.
 *
 * @SpringBootTest loads the full application context, so this tests
 * the entire flow: Controller → Service → Repository → H2.
 *
 * Run with: mvn test
 */
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class StudentApiApplicationTests {

    @Autowired
    private StudentService studentService;

    @Autowired
    private StudentRepository studentRepository;

    private static Long createdId;

    @BeforeEach
    void cleanUp() {
        studentRepository.deleteAll();
    }

    // ─── CREATE ─────────────────────────────────────────────────────────────

    @Test
    @Order(1)
    @DisplayName("Should create a student and return with generated ID")
    void testCreateStudent() {
        StudentRequestDTO req = StudentRequestDTO.builder()
                .firstName("Jane").lastName("Doe")
                .email("jane.doe@test.com")
                .age(20).course("CS").build();

        StudentResponseDTO res = studentService.createStudent(req);

        assertThat(res.getId()).isNotNull();
        assertThat(res.getEmail()).isEqualTo("jane.doe@test.com");
        assertThat(res.getFirstName()).isEqualTo("Jane");

        createdId = res.getId();
    }

    // ─── GET ALL ─────────────────────────────────────────────────────────────

    @Test
    @Order(2)
    @DisplayName("Should return all students")
    void testGetAllStudents() {
        studentService.createStudent(StudentRequestDTO.builder()
                .firstName("A").lastName("B").email("a@b.com").age(19).course("Math").build());

        List<StudentResponseDTO> students = studentService.getAllStudents();
        assertThat(students).hasSize(1);
    }

    // ─── GET BY ID ───────────────────────────────────────────────────────────

    @Test
    @Order(3)
    @DisplayName("Should fetch a student by ID")
    void testGetStudentById() {
        StudentResponseDTO created = studentService.createStudent(StudentRequestDTO.builder()
                .firstName("X").lastName("Y").email("x@y.com").age(21).course("Biology").build());

        StudentResponseDTO fetched = studentService.getStudentById(created.getId());
        assertThat(fetched.getEmail()).isEqualTo("x@y.com");
    }

    @Test
    @Order(4)
    @DisplayName("Should throw ResourceNotFoundException for unknown ID")
    void testGetStudentByIdNotFound() {
        assertThatThrownBy(() -> studentService.getStudentById(999L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("999");
    }

    // ─── UPDATE ──────────────────────────────────────────────────────────────

    @Test
    @Order(5)
    @DisplayName("Should update a student successfully")
    void testUpdateStudent() {
        StudentResponseDTO created = studentService.createStudent(StudentRequestDTO.builder()
                .firstName("Old").lastName("Name").email("old@email.com").age(18).course("Art").build());

        StudentRequestDTO update = StudentRequestDTO.builder()
                .firstName("New").lastName("Name").email("old@email.com").age(19).course("Design").build();

        StudentResponseDTO updated = studentService.updateStudent(created.getId(), update);
        assertThat(updated.getFirstName()).isEqualTo("New");
        assertThat(updated.getCourse()).isEqualTo("Design");
    }

    // ─── DELETE ──────────────────────────────────────────────────────────────

    @Test
    @Order(6)
    @DisplayName("Should delete a student and confirm removal")
    void testDeleteStudent() {
        StudentResponseDTO created = studentService.createStudent(StudentRequestDTO.builder()
                .firstName("Del").lastName("Me").email("del@me.com").age(25).course("Law").build());

        studentService.deleteStudent(created.getId());

        assertThatThrownBy(() -> studentService.getStudentById(created.getId()))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}
