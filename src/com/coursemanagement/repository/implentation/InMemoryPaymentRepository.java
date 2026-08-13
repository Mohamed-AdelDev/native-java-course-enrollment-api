package com.coursemanagement.repository.implentation;

import com.coursemanagement.model.Payment;
import com.coursemanagement.repository.PaymentRepository;

import java.util.HashMap;
import java.util.*;
import java.util.Optional;

public class InMemoryPaymentRepository implements PaymentRepository {
    private final Map<Long,Payment>payments = new HashMap<>();

    private long nextId = 1l;


    @Override
    public Payment save(Payment payment) {

        if (payment.getId() == null) {
            payment.setId(nextId++);
        }

        payments.put(payment.getId(), payment);

        return payment;
    }

    @Override
    public Optional<Payment> findById(Long id) {
        return Optional.ofNullable(payments.get(id));
    }

    @Override
    public Optional<Payment> findByEnrollmentId(Long enrollmentId) {

        for (Payment payment : payments.values()) {

            if (payment.getEnrollmentId().equals(enrollmentId)) {
                return Optional.of(payment);
            }

        }

        return Optional.empty();
    }

    @Override
    public List<Payment> findAll() {
        return new ArrayList<>(payments.values());
    }
}
