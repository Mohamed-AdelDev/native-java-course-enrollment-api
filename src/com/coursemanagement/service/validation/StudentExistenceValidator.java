package com.coursemanagement.service.validation;

import com.coursemanagement.dto.request.CreateEnrollmentRequest;
import com.coursemanagement.repository.StudentRepository;

public class StudentExistenceValidator implements EnrollmentValidator {

    private final StudentRepository studentRepository;
    private EnrollmentValidator next;

    public StudentExistenceValidator(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public void setNext(EnrollmentValidator next) {
        this.next = next;
    }

    @Override
    public void validate(CreateEnrollmentRequest request) {

        if (request.getStudentId() == null
                || !studentRepository.findById(request.getStudentId()).isPresent()) {

            throw new IllegalArgumentException("Student not found");
        }

        if (next != null) {
            next.validate(request);
        }
    }
}