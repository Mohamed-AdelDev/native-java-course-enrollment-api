package com.coursemanagement.service;

import com.coursemanagement.dto.request.CreateEnrollmentRequest;
import com.coursemanagement.dto.request.CreatePaymentRequest;
import com.coursemanagement.dto.response.EnrollmentResponse;
import com.coursemanagement.dto.response.PaymentResponse;
import com.coursemanagement.event.EnrollmentConfirmedEvent;
import com.coursemanagement.event.EventPublisher;
import com.coursemanagement.model.Enrollment;
import com.coursemanagement.model.enums.PaymentStatus;
import com.coursemanagement.repository.EnrollmentRepository;

import java.util.List;

public class EnrollmentFacade {

    private final EnrollmentService enrollmentService;
    private final PaymentService paymentService;
    private final EnrollmentRepository enrollmentRepository;
    private final EventPublisher eventPublisher;

    public EnrollmentFacade(
            EnrollmentService enrollmentService,
            PaymentService paymentService,
            EnrollmentRepository enrollmentRepository,
            EventPublisher eventPublisher) {

        this.enrollmentService = enrollmentService;
        this.paymentService = paymentService;
        this.enrollmentRepository = enrollmentRepository;
        this.eventPublisher = eventPublisher;
    }

    public EnrollmentResponse createEnrollment(
            CreateEnrollmentRequest request) {

        return enrollmentService.createEnrollment(request);
    }

    public EnrollmentResponse getMyEnrollment(
            Long enrollmentId,
            Long studentId) {

        return enrollmentService.findMyEnrollment(
                enrollmentId,
                studentId
        );
    }

    public List<EnrollmentResponse> getMyEnrollments(
            Long studentId) {

        return enrollmentService.findMyEnrollments(studentId);
    }

    public void deleteEnrollment(
            Long enrollmentId) {

        enrollmentService.deleteEnrollment(enrollmentId);
    }

    public PaymentResponse payEnrollment(
            Long enrollmentId,
            CreatePaymentRequest request) {

        PaymentResponse payment =
                paymentService.createPayment(
                        enrollmentId,
                        request
                );

        if (payment.getPaymentStatus() == PaymentStatus.PAID) {

            Enrollment enrollment =
                    enrollmentRepository
                            .findById(enrollmentId)
                            .orElseThrow(() ->
                                    new IllegalArgumentException(
                                            "Enrollment not found"
                                    ));

            eventPublisher.publish(
                    new EnrollmentConfirmedEvent(
                            enrollment.getId(),
                            enrollment.getStudentId(),
                            enrollment.getCourseId()
                    )
            );
        }

        return payment;
    }
}