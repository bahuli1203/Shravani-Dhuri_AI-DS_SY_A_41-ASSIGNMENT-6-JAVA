package com.example.studentapi.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * JPA Entity – maps to the "students" table in the H2 database.
 *
 * This class is ONLY used inside the Repository and Service layers.
 * Controllers never touch this class directly; they work with DTOs.
 */
@Entity
@Table(name = "students")
@Data                   // Lombok: generates getters, setters, equals, hashCode, toString
@NoArgsConstructor      // Lombok: required by JPA
@AllArgsConstructor     // Lombok: convenience constructor
@Builder                // Lombok: fluent builder for Service layer conversions
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String firstName;

    @Column(nullable = false, length = 100)
    private String lastName;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @Column(nullable = false)
    private int age;

    @Column(nullable = false, length = 100)
    private String course;
}
