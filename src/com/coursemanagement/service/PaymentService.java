package com.coursemanagement.service;

import com.coursemanagement.dto.request.CreatePaymentRequest;
import com.coursemanagement.dto.mapper.PaymentMapper;
import com.coursemanagement.dto.response.PaymentResponse;
import com.coursemanagement.model.Payment;
import com.coursemanagement.repository.PaymentRepository;

public class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public PaymentResponse createPayment(CreatePaymentRequest request,
                                         Payment payment) {

        paymentRepository.save(payment);

        return PaymentMapper.toResponse(payment);
    }

}