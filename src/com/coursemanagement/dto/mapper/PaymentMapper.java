package com.coursemanagement.dto.mapper;

import com.coursemanagement.dto.response.PaymentResponse;
import com.coursemanagement.model.Payment;

public class PaymentMapper {

    public static PaymentResponse toResponse(
            Payment payment) {

        return new PaymentResponse(
                payment.getId(),
                payment.getEnrollmentId(),
                payment.getAmount(),
                payment.getPaymentMethod(),
                payment.getPaymentStatus(),
                payment.getTransactionReference(),
                payment.getPaymentDate()
        );
    }
}