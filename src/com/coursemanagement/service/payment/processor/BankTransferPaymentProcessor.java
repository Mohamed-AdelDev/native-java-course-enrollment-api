package com.coursemanagement.service.payment.processor;

import com.coursemanagement.model.Enrollment;
import com.coursemanagement.model.enums.EnrollmentStatus;
import com.coursemanagement.repository.CourseRepository;
import com.coursemanagement.repository.EnrollmentRepository;
import com.coursemanagement.repository.PaymentRepository;
import com.coursemanagement.service.payment.PaymentGateway;
import com.coursemanagement.service.payment.command.PaymentCommand;

public class BankTransferPaymentProcessor
        extends AbstractPaymentProcessor {

    public BankTransferPaymentProcessor(
            EnrollmentRepository enrollmentRepository,
            PaymentRepository paymentRepository,
            CourseRepository courseRepository,
            PaymentGateway paymentGateway) {

        super(
                enrollmentRepository,
                paymentRepository,
                courseRepository,
                paymentGateway
        );
    }

    @Override
    protected void validate(
            PaymentCommand command) {

        Enrollment enrollment =
                enrollmentRepository
                        .findById(command.getEnrollmentId())
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Enrollment not found"
                                ));

        if (enrollment.getStatus()
                != EnrollmentStatus.Pendig) {

            throw new IllegalArgumentException(
                    "Enrollment is not pending payment"
            );
        }

        if (paymentRepository
                .findByEnrollmentId(
                        command.getEnrollmentId()
                )
                .isPresent()) {

            throw new IllegalArgumentException(
                    "Enrollment has already been paid"
            );
        }

        if (command.getAmount()
                .compareTo(
                        enrollment.getFinalPrice()
                ) != 0) {

            throw new IllegalArgumentException(
                    "Payment amount must equal enrollment final price"
            );
        }
    }
}