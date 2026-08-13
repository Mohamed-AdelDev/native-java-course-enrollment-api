package com.coursemanagement.repository.implentation;

import com.coursemanagement.model.Student;
import com.coursemanagement.model.enums.Role;
import com.coursemanagement.repository.CourseRepository;
import com.coursemanagement.repository.StudentRepository;
import com.coursemanagement.repository.TokenRepository;

import java.time.LocalDateTime;

public class RepositoryManager {

    public static final StudentRepository studentRepository =
            new InMemoryStudentRepository();

    public static final TokenRepository tokenRepository =
            new InMemoryTokenRepository();

    public static final CourseRepository courseRepository =
            new InMemoryCourseRepository();

    static {

        Student admin = new Student(
                "Admin",
                "admin@test.com",
                "123456",
                Role.Admin,
                true,
                LocalDateTime.now()
        );

        studentRepository.save(admin);

        Student student = new Student(
                "Test Student",
                "student@test.com",
                "123456",
                Role.Student,
                true,
                LocalDateTime.now()
        );

        studentRepository.save(student);
    }
}