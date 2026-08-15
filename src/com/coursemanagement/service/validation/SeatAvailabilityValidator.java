package com.coursemanagement.service.validation;

import com.coursemanagement.dto.request.CreateEnrollmentRequest;
import com.coursemanagement.repository.CourseRepository;

public class SeatAvailabilityValidator implements EnrollmentValidator {

    private final CourseRepository courseRepository;
    private EnrollmentValidator next;

    public SeatAvailabilityValidator(CourseRepository courseRepository) {
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

        if (course.getAvailableSeats() <= 0) {
            throw new IllegalArgumentException(
                    "No available seats"
            );
        }

        if (next != null) {
            next.validate(request);
        }
    }
}