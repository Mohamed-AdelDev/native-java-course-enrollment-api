package com.coursemanagement.service;

import com.coursemanagement.dto.request.CreatePaymentRequest;
import com.coursemanagement.dto.response.PaymentResponse;
import com.coursemanagement.dto.mapper.PaymentMapper;
import com.coursemanagement.model.Enrollment;
import com.coursemanagement.model.Payment;
import com.coursemanagement.model.enums.PaymentMethod;
import com.coursemanagement.repository.CourseRepository;
import com.coursemanagement.repository.EnrollmentRepository;
import com.coursemanagement.repository.PaymentRepository;
import com.coursemanagement.service.payment.PaymentGateway;
import com.coursemanagement.service.payment.PaymentGatewayFactory;
import com.coursemanagement.service.payment.command.PaymentCommand;
import com.coursemanagement.service.payment.processor.AbstractPaymentProcessor;
import com.coursemanagement.service.payment.processor.BankTransferPaymentProcessor;
import com.coursemanagement.service.payment.processor.CardPaymentProcessor;
import com.coursemanagement.service.payment.processor.WalletPaymentProcessor;

public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final CourseRepository courseRepository;

    public PaymentService(
            PaymentRepository paymentRepository,
            EnrollmentRepository enrollmentRepository,
            CourseRepository courseRepository) {

        this.paymentRepository = paymentRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.courseRepository = courseRepository;
    }

    public PaymentResponse createPayment(
            Long enrollmentId,
            CreatePaymentRequest request) {

        Enrollment enrollment =
                enrollmentRepository
                        .findById(enrollmentId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Enrollment not found"
                                ));

        PaymentMethod paymentMethod;

        try {

            paymentMethod =
                    PaymentMethod.valueOf(
                            request.getPaymentMethod()
                                    .toUpperCase()
                    );

        } catch (Exception e) {

            throw new IllegalArgumentException(
                    "Unsupported payment method"
            );
        }

        if (request.getPaymentReference() == null
                || request.getPaymentReference().isBlank()) {

            throw new IllegalArgumentException(
                    "Payment reference is required"
            );
        }

        PaymentCommand command =
                new PaymentCommand.Builder()
                        .enrollmentId(enrollmentId)
                        .amount(enrollment.getFinalPrice())
                        .paymentMethod(paymentMethod)
                        .paymentReference(
                                request.getPaymentReference()
                        )
                        .build();

        PaymentGateway gateway =
                PaymentGatewayFactory.getGateway(
                        paymentMethod
                );

        AbstractPaymentProcessor processor;

        switch (paymentMethod) {

            case CARD ->
                    processor =
                            new CardPaymentProcessor(
                                    enrollmentRepository,
                                    paymentRepository,
                                    courseRepository,
                                    gateway
                            );

            case WALLET ->
                    processor =
                            new WalletPaymentProcessor(
                                    enrollmentRepository,
                                    paymentRepository,
                                    courseRepository,
                                    gateway
                            );

            case BANK_TRANSFER ->
                    processor =
                            new BankTransferPaymentProcessor(
                                    enrollmentRepository,
                                    paymentRepository,
                                    courseRepository,
                                    gateway
                            );

            default ->
                    throw new IllegalArgumentException(
                            "Unsupported payment method"
                    );
        }

        Payment payment =
                processor.process(command);

        return PaymentMapper.toResponse(payment);
    }
}