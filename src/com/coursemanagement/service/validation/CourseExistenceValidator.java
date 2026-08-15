package com.coursemanagement.service.validation;

import com.coursemanagement.dto.request.CreateEnrollmentRequest;
import com.coursemanagement.repository.CourseRepository;

public class CourseExistenceValidator implements EnrollmentValidator {

    private final CourseRepository courseRepository;
    private EnrollmentValidator next;

    public CourseExistenceValidator(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public void setNext(EnrollmentValidator next) {
        this.next = next;
    }

    @Override
    public void validate(CreateEnrollmentRequest request) {

        if (request.getCourseId() == null
                || !courseRepository.findById(request.getCourseId()).isPresent()) {

            throw new IllegalArgumentException("Course not found");
        }

        if (next != null) {
            next.validate(request);
        }
    }
}