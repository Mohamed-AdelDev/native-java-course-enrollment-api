package com.coursemanagement.service.validation;

import com.coursemanagement.dto.request.CreateEnrollmentRequest;
import com.coursemanagement.repository.EnrollmentRepository;

public class DuplicateEnrollmentValidator implements EnrollmentValidator {

    private final EnrollmentRepository enrollmentRepository;
    private EnrollmentValidator next;

    public DuplicateEnrollmentValidator(
            EnrollmentRepository enrollmentRepository) {

        this.enrollmentRepository = enrollmentRepository;
    }

    @Override
    public void setNext(EnrollmentValidator next) {
        this.next = next;
    }

    @Override
    public void validate(CreateEnrollmentRequest request) {

        if (enrollmentRepository.existsByStudentIdAndCourseId(
                request.getStudentId(),
                request.getCourseId())) {

            throw new IllegalArgumentException(
                    "Student is already enrolled in this course"
            );
        }

        if (next != null) {
            next.validate(request);
        }
    }
}