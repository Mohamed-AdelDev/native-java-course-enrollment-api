package com.coursemanagement.main;

import com.coursemanagement.dto.request.CreateCourseRequest;
import com.coursemanagement.dto.request.RegisterStudentRequest;
import com.coursemanagement.dto.response.CourseResponse;
import com.coursemanagement.dto.response.StudentResponse;
import com.coursemanagement.enums.*;
import com.coursemanagement.model.*;
import com.coursemanagement.repository.*;
import com.coursemanagement.service.CourseService;
import com.coursemanagement.service.StudentService;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Application {

    public static void main(String[] args) {

        System.out.println("Course Enrollment Management System");
        System.out.println("Application started successfully\n");



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
        System.out.println("========================================");

        StudentRepository studentRepository = new InMemoryStudentRepository();
        CourseRepository courseRepository = new InMemoryCourseRepository();
        EnrollmentRepository enrollmentRepository = new InMemoryEnrollmentRepository();

        studentRepository.save(student);

        System.out.println("\nSaved Student:");
        System.out.println(student);

        System.out.println("\nFind Student By ID:");
        System.out.println(studentRepository.findById(student.getId()));

        System.out.println("\nFind Student By Email:");
        System.out.println(studentRepository.findByEmail(student.getEmail()));

        courseRepository.save(course);

        course.setTitle("Advanced Java");
        courseRepository.save(course);

        System.out.println("\nUpdated Course:");
        System.out.println(courseRepository.findById(course.getId()));

        courseRepository.deleteById(course.getId());

        System.out.println("\nCourses After Delete:");
        System.out.println(courseRepository.findAll());

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


        System.out.println("\n========================================");
        System.out.println("Service Layer Testing");
        System.out.println("========================================");


        StudentRepository serviceStudentRepository = new InMemoryStudentRepository();
        CourseRepository serviceCourseRepository = new InMemoryCourseRepository();

        StudentService studentService = new StudentService(serviceStudentRepository);
        CourseService courseService = new CourseService(serviceCourseRepository);

        RegisterStudentRequest registerRequest =
                new RegisterStudentRequest(
                        "Mohamed Adel",
                        "service@gmail.com",
                        "123456"
                );

        StudentResponse studentResponse =
                studentService.registerStudent(registerRequest);

        System.out.println("\nRegistered Student:");
        System.out.println(studentResponse);

        CreateCourseRequest createCourseRequest =
                new CreateCourseRequest(
                        "Java",
                        "Native Java Course",
                        new BigDecimal("500"),
                        30
                );

        CourseResponse courseResponse =
                courseService.createCourse(createCourseRequest);

        System.out.println("\nCreated Course:");
        System.out.println(courseResponse);

        System.out.println("\nFind Student:");
        System.out.println(studentService.findStudentById(studentResponse.getId()));

        System.out.println("\nFind Course:");
        System.out.println(courseService.findCourseById(courseResponse.getId()));

        System.out.println("\nAll Students:");
        System.out.println(studentService.findAllStudents());

        System.out.println("\nAll Courses:");
        System.out.println(courseService.findAllCourses());
    }
}