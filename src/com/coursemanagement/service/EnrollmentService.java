package com.coursemanagement.service;

import com.coursemanagement.dto.mapper.EnrollmentMapper;

import com.coursemanagement.dto.request.CreateEnrollmentRequest;

import com.coursemanagement.dto.response.EnrollmentResponse;
import com.coursemanagement.model.Enrollment;
import com.coursemanagement.repository.EnrollmentRepository;

public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;

    public EnrollmentService(EnrollmentRepository enrollmentRepository) {
        this.enrollmentRepository = enrollmentRepository;
    }

    public EnrollmentResponse createEnrollment(CreateEnrollmentRequest request,
                                               Enrollment enrollment) {

        enrollmentRepository.save(enrollment);

        return EnrollmentMapper.toResponse(enrollment);
    }

}