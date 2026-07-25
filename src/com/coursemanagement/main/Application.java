package com.coursemanagement.main;

import com.coursemanagement.enums.*;
import com.coursemanagement.model.*;
import com.coursemanagement.repository.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
public class Application {

    public static void main(String[] args) {

        Student student = new Student(
                "Mohamed Adel",
                "mohamed@gmail.com",
                "123456",
                Role.Student,
                true,
                LocalDateTime.now()
        );

        Course course = new Course(
                "Java",
                "Native Java Course",
                new BigDecimal("500"),
                30,
                30,
                CourseStatus.Open,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        Enrollment enrollment = new Enrollment(
                1L,
                1L,
                new BigDecimal("500"),
                BigDecimal.ZERO,
                new BigDecimal("500"),
                EnrollmentStatus.Enrolled,
                LocalDateTime.now()
        );

        Payment payment = new Payment(
                1L,
                new BigDecimal("500"),
                PaymentMethod.CreditCard,
                PaymentStatus.Paid,
                "TXN-1001",
                LocalDateTime.now()
        );

        AuditLog auditLog = new AuditLog(
                "CREATE",
                "Student",
                1L,
                "Student registered",
                LocalDateTime.now()
        );

        System.out.println(student);
        System.out.println(course);
        System.out.println(enrollment);
        System.out.println(payment);
        System.out.println(auditLog);

        System.out.println("\n========================================");
        System.out.println("Repository Layer Testing");
        System.out.println("========================================\n");

        StudentRepository studentRepository = new InMemoryStudentRepository();
        CourseRepository courseRepository = new InMemoryCourseRepository();
        EnrollmentRepository enrollmentRepository = new InMemoryEnrollmentRepository();



        Student students = new Student(
                "Mohamed Adel",
                "mohamed@gmail.com",
                "123456",
                Role.Student,
                true,
                LocalDateTime.now()
        );

        studentRepository.save(student);

        System.out.println("Saved Student:");
        System.out.println(student);



        System.out.println("\nFind Student By ID:");
        System.out.println(studentRepository.findById(student.getId()));



        System.out.println("\nFind Student By Email:");
        System.out.println(studentRepository.findByEmail("mohamed@gmail.com"));



        Course courses = new Course(
                "Java",
                "Native Java Course",
                new BigDecimal("500"),
                30,
                30,
                CourseStatus.Open,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        courseRepository.save(course);


        course.setTitle("Advanced Java");

        courseRepository.save(course);

        System.out.println("\nUpdated Course:");
        System.out.println(courseRepository.findById(course.getId()));


        courseRepository.deleteById(course.getId());

        System.out.println("\nCourses After Delete:");
        System.out.println(courseRepository.findAll());



        Enrollment enrollments = new Enrollment(
                student.getId(),
                1L,
                new BigDecimal("500"),
                BigDecimal.ZERO,
                new BigDecimal("500"),
                EnrollmentStatus.Enrolled,
                LocalDateTime.now()
        );

        enrollmentRepository.save(enrollment);


        System.out.println("\nEnrollments:");
        System.out.println(enrollmentRepository.findByStudentId(student.getId()));


        System.out.println("\nDuplicate Enrollment:");
        System.out.println(
                enrollmentRepository.existsByStudentIdAndCourseId(
                        student.getId(),
                        1L
                )
        );
    }
}