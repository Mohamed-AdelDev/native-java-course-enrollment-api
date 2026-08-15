package com.coursemanagement.service;

import com.coursemanagement.dto.mapper.EnrollmentMapper;
import com.coursemanagement.dto.request.CreateEnrollmentRequest;
import com.coursemanagement.dto.response.EnrollmentResponse;
import com.coursemanagement.model.Course;
import com.coursemanagement.model.Enrollment;
import com.coursemanagement.model.enums.DiscountType;
import com.coursemanagement.model.enums.EnrollmentStatus;
import com.coursemanagement.repository.CourseRepository;
import com.coursemanagement.repository.EnrollmentRepository;
import com.coursemanagement.service.discount.DiscountStrategy;
import com.coursemanagement.service.discount.DiscountStrategyFactory;
import com.coursemanagement.service.validation.CourseActiveValidator;
import com.coursemanagement.service.validation.CourseExistenceValidator;
import com.coursemanagement.service.validation.DuplicateEnrollmentValidator;
import com.coursemanagement.service.validation.EnrollmentValidator;
import com.coursemanagement.service.validation.SeatAvailabilityValidator;
import com.coursemanagement.service.validation.StudentExistenceValidator;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final CourseRepository courseRepository;

    private final EnrollmentValidator validationChain;

    public EnrollmentService(
            EnrollmentRepository enrollmentRepository,
            CourseRepository courseRepository) {

        this.enrollmentRepository = enrollmentRepository;
        this.courseRepository = courseRepository;

        StudentExistenceValidator studentValidator =
                new StudentExistenceValidator(
                        com.coursemanagement.repository.implentation.RepositoryManager.studentRepository
                );

        CourseExistenceValidator courseValidator =
                new CourseExistenceValidator(courseRepository);

        CourseActiveValidator activeValidator =
                new CourseActiveValidator(courseRepository);

        SeatAvailabilityValidator seatValidator =
                new SeatAvailabilityValidator(courseRepository);

        DuplicateEnrollmentValidator duplicateValidator =
                new DuplicateEnrollmentValidator(enrollmentRepository);

        studentValidator.setNext(courseValidator);
        courseValidator.setNext(activeValidator);
        activeValidator.setNext(seatValidator);
        seatValidator.setNext(duplicateValidator);

        this.validationChain = studentValidator;
    }

    public EnrollmentResponse createEnrollment(
            CreateEnrollmentRequest request) {

        validationChain.validate(request);

        Course course =
                courseRepository.findById(request.getCourseId())
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Course not found"
                                ));

        DiscountStrategy strategy =
                DiscountStrategyFactory.getStrategy(
                        DiscountType.valueOf(
                                request.getDiscountType().toUpperCase()
                        )
                );

        BigDecimal originalPrice =
                course.getPrice();

        BigDecimal discountAmount =
                strategy.calculateDiscount(originalPrice);

        BigDecimal finalPrice =
                originalPrice.subtract(discountAmount);

        if (finalPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(
                    "Final price cannot be negative"
            );
        }

        Enrollment enrollment =
                new Enrollment(
                        request.getStudentId(),
                        request.getCourseId(),
                        originalPrice,
                        discountAmount,
                        finalPrice,
                        EnrollmentStatus.Pendig,
                        LocalDateTime.now()
                );

        enrollmentRepository.save(enrollment);

        return EnrollmentMapper.toResponse(enrollment);
    }

    public List<EnrollmentResponse> findMyEnrollments(
            Long studentId) {

        return enrollmentRepository.findByStudentId(studentId)
                .stream()
                .map(EnrollmentMapper::toResponse)
                .collect(Collectors.toList());
    }

    public EnrollmentResponse findMyEnrollment(
            Long enrollmentId,
            Long studentId) {

        Enrollment enrollment =
                enrollmentRepository.findById(enrollmentId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Enrollment not found"
                                ));

        if (!enrollment.getStudentId().equals(studentId)) {
            throw new IllegalArgumentException(
                    "Enrollment not found"
            );
        }

        return EnrollmentMapper.toResponse(enrollment);
    }

    public void deleteEnrollment(Long id) {

        if (!enrollmentRepository.findById(id).isPresent()) {
            throw new IllegalArgumentException(
                    "Enrollment not found"
            );
        }

        enrollmentRepository.deleteById(id);
    }
}