package com.coursemanagement.service.validation;

import com.coursemanagement.dto.request.CreateEnrollmentRequest;
import com.coursemanagement.model.enums.CourseStatus;
import com.coursemanagement.repository.CourseRepository;

public class CourseActiveValidator implements EnrollmentValidator {

    private final CourseRepository courseRepository;
    private EnrollmentValidator next;

    public CourseActiveValidator(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public void setNext(EnrollmentValidator next) {
        this.next = next;
    }

    @Override
    public void validate(CreateEnrollmentRequest request) {

        var course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() ->
                        new IllegalArgumentException("Course not found"));

        if (course.getStatus() != CourseStatus.Open) {
            throw new IllegalArgumentException(
                    "Course is not active"
            );
        }

        if (next != null) {
            next.validate(request);
        }
    }
}