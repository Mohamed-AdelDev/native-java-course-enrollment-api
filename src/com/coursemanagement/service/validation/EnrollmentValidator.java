package com.coursemanagement.service.validation;

import com.coursemanagement.dto.request.CreateEnrollmentRequest;

public interface EnrollmentValidator {

    void setNext(EnrollmentValidator next);

    void validate(CreateEnrollmentRequest request);
}