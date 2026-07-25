package com.coursemanagement.dto.mapper;

import com.coursemanagement.dto.response.EnrollmentResponse;
import com.coursemanagement.model.Enrollment;


public class EnrollmentMapper {

    public static EnrollmentResponse toResponse(Enrollment enrollment) {

        return new EnrollmentResponse(
                enrollment.getId(),
                enrollment.getStudentId(),
                enrollment.getCourseId(),
                enrollment.getOriginalPrice(),
                enrollment.getDiscountAmount(),
                enrollment.getFinalPrice(),
                enrollment.getStatus(),
                enrollment.getEnrollmentDate()
        );
    }
}