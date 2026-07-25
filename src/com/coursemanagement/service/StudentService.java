package com.coursemanagement.service;

import com.coursemanagement.dto.request.RegisterStudentRequest;
import com.coursemanagement.dto.mapper.StudentMapper;
import com.coursemanagement.dto.response.StudentResponse;
import com.coursemanagement.enums.Role;
import com.coursemanagement.model.Student;
import com.coursemanagement.repository.StudentRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public StudentResponse registerStudent(RegisterStudentRequest request) {

        if (request.getFullName() == null || request.getFullName().isBlank()) {
            throw new IllegalArgumentException("Full name is required");
        }

        if (request.getEmail() == null || request.getEmail().isBlank()) {
            throw new IllegalArgumentException("Email is required");
        }

        if (!request.getEmail().contains("@")) {
            throw new IllegalArgumentException("Invalid email");
        }

        if (studentRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        if (request.getPassword() == null || request.getPassword().length() < 6) {
            throw new IllegalArgumentException("Password must contain at least 6 characters");
        }

        Student student = new Student(
                request.getFullName(),
                request.getEmail(),
                request.getPassword(),
                Role.Student,
                true,
                LocalDateTime.now()
        );

        studentRepository.save(student);

        return StudentMapper.toResponse(student);
    }

    public StudentResponse findStudentById(Long id) {

        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));

        return StudentMapper.toResponse(student);
    }

    public List<StudentResponse> findAllStudents() {

        return studentRepository.findAll()
                .stream()
                .map(StudentMapper::toResponse)
                .collect(Collectors.toList());
    }
}