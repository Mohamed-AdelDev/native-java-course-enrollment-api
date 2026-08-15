package com.coursemanagement.repository.implentation;

import com.coursemanagement.model.Student;
import com.coursemanagement.model.enums.Role;
import com.coursemanagement.repository.*;

import java.time.LocalDateTime;

public class RepositoryManager {

    public static final StudentRepository studentRepository =
            new InMemoryStudentRepository();

    public static final TokenRepository tokenRepository =
            new InMemoryTokenRepository();

    public static final CourseRepository courseRepository =
            new InMemoryCourseRepository();

    public static final EnrollmentRepository enrollmentRepository =
            new InMemoryEnrollmentRepository();

    public static final PaymentRepository paymentRepository =
            new InMemoryPaymentRepository();

    public static final AuditLogRepository auditLogRepository =
            new InMemoryAuditLogRepository();

    static {

        Student admin =
                new Student(
                        "Admin",
                        "admin@test.com",
                        "123456",
                        Role.Admin,
                        true,
                        LocalDateTime.now()
                );

        studentRepository.save(admin);

        Student student =
                new Student(
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