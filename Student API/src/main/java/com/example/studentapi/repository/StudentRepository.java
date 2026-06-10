package com.example.studentapi.repository;

import com.example.studentapi.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * LAYER 3 – REPOSITORY LAYER
 *
 * Extends JpaRepository to get full CRUD + pagination support out of the box.
 * Spring Data JPA auto-generates the SQL implementation at runtime.
 *
 * JpaRepository<Student, Long>
 *   └── Student : the entity type to manage
 *   └── Long    : the type of the primary key (@Id)
 *
 * Built-in methods inherited (no code needed):
 *   save(), findById(), findAll(), deleteById(), existsById(), count() …
 */
@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    /**
     * Custom derived query – Spring Data JPA generates the SQL automatically
     * from the method name: SELECT * FROM students WHERE email = ?
     */
    Optional<Student> findByEmail(String email);

    /**
     * Check whether an email is already registered (used for duplicate validation).
     */
    boolean existsByEmail(String email);
}
