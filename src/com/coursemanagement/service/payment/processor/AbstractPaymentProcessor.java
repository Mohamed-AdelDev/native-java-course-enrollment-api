package com.coursemanagement.service.payment.processor;

import com.coursemanagement.model.Course;
import com.coursemanagement.model.Enrollment;
import com.coursemanagement.model.Payment;
import com.coursemanagement.model.enums.EnrollmentStatus;
import com.coursemanagement.model.enums.PaymentStatus;
import com.coursemanagement.repository.CourseRepository;
import com.coursemanagement.repository.EnrollmentRepository;
import com.coursemanagement.repository.PaymentRepository;
import com.coursemanagement.service.payment.PaymentGateway;
import com.coursemanagement.service.payment.command.PaymentCommand;

import java.time.LocalDateTime;

public abstract class AbstractPaymentProcessor {

    protected final EnrollmentRepository enrollmentRepository;
    protected final PaymentRepository paymentRepository;
    protected final CourseRepository courseRepository;
    protected final PaymentGateway paymentGateway;

    public AbstractPaymentProcessor(
            EnrollmentRepository enrollmentRepository,
            PaymentRepository paymentRepository,
            CourseRepository courseRepository,
            PaymentGateway paymentGateway) {

        this.enrollmentRepository =
                enrollmentRepository;

        this.paymentRepository =
                paymentRepository;

        this.courseRepository =
                courseRepository;

        this.paymentGateway =
                paymentGateway;
    }

    public Payment process(
            PaymentCommand command) {

        // 1. Validate request
        validate(command);

        // 2. Create payment command
        PaymentCommand paymentCommand =
                createPaymentCommand(command);

        // 3. Execute provider payment
        boolean success =
                executeProviderPayment(
                        paymentCommand
                );

        // 4. Create payment record
        Payment payment =
                createPaymentRecord(
                        command,
                        success
                );

        // 5. Update enrollment
        updateEnrollment(
                command,
                success
        );

        // 6. Post-payment actions
        performPostPaymentActions(
                command,
                success
        );

        return payment;
    }

    protected abstract void validate(
            PaymentCommand command
    );

    protected PaymentCommand createPaymentCommand(
            PaymentCommand command) {

        return command;
    }

    protected boolean executeProviderPayment(
            PaymentCommand command) {

        return paymentGateway.pay(command);
    }

    protected Payment createPaymentRecord(
            PaymentCommand command,
            boolean success) {

        Payment payment =
                new Payment(
                        command.getEnrollmentId(),
                        command.getAmount(),
                        command.getPaymentMethod(),
                        success
                                ? PaymentStatus.PAID
                                : PaymentStatus.FAILED,
                        command.getPaymentReference(),
                        LocalDateTime.now()
                );

        return paymentRepository.save(payment);
    }

    protected void updateEnrollment(
            PaymentCommand command,
            boolean success) {

        if (!success) {
            return;
        }

        Enrollment enrollment =
                enrollmentRepository
                        .findById(
                                command.getEnrollmentId()
                        )
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Enrollment not found"
                                )
                        );

        enrollment.setStatus(
                EnrollmentStatus.CONFIRMED
        );

        enrollmentRepository.save(
                enrollment
        );
    }

    protected void performPostPaymentActions(
            PaymentCommand command,
            boolean success) {

        if (!success) {
            return;
        }

        Enrollment enrollment =
                enrollmentRepository
                        .findById(
                                command.getEnrollmentId()
                        )
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Enrollment not found"
                                )
                        );

        Course course =
                courseRepository
                        .findById(
                                enrollment.getCourseId()
                        )
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Course not found"
                                )
                        );

        if (course.getAvailableSeats() > 0) {

            course.setAvailableSeats(
                    course.getAvailableSeats() - 1
            );

            courseRepository.save(course);
        }
    }
}