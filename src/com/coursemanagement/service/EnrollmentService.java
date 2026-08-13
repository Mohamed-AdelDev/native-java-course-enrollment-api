package com.coursemanagement.service;

import com.coursemanagement.dto.mapper.EnrollmentMapper;
import com.coursemanagement.dto.request.CreateEnrollmentRequest;
import com.coursemanagement.dto.response.EnrollmentResponse;
import com.coursemanagement.model.Enrollment;
import com.coursemanagement.repository.EnrollmentRepository;

import java.util.List;
import java.util.stream.Collectors;

public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;

    public EnrollmentService(EnrollmentRepository enrollmentRepository) {
        this.enrollmentRepository = enrollmentRepository;
    }

    public EnrollmentResponse createEnrollment(
            CreateEnrollmentRequest request,
            Enrollment enrollment) {

        enrollmentRepository.save(enrollment);

        return EnrollmentMapper.toResponse(enrollment);
    }

    public List<EnrollmentResponse> findMyEnrollments(Long studentId) {

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
}