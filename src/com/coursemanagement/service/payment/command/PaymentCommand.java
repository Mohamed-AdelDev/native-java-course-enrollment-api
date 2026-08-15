package com.coursemanagement.service.payment.command;

import com.coursemanagement.model.enums.PaymentMethod;

import java.math.BigDecimal;

public class PaymentCommand {

    private final Long enrollmentId;
    private final BigDecimal amount;
    private final PaymentMethod paymentMethod;
    private final String paymentReference;

    private PaymentCommand(Builder builder) {

        this.enrollmentId =
                builder.enrollmentId;

        this.amount =
                builder.amount;

        this.paymentMethod =
                builder.paymentMethod;

        this.paymentReference =
                builder.paymentReference;
    }

    public Long getEnrollmentId() {
        return enrollmentId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public String getPaymentReference() {
        return paymentReference;
    }

    public static class Builder {

        private Long enrollmentId;
        private BigDecimal amount;
        private PaymentMethod paymentMethod;
        private String paymentReference;

        public Builder enrollmentId(Long enrollmentId) {

            this.enrollmentId = enrollmentId;

            return this;
        }

        public Builder amount(BigDecimal amount) {

            this.amount = amount;

            return this;
        }

        public Builder paymentMethod(
                PaymentMethod paymentMethod) {

            this.paymentMethod = paymentMethod;

            return this;
        }

        public Builder paymentReference(
                String paymentReference) {

            this.paymentReference =
                    paymentReference;

            return this;
        }

        public PaymentCommand build() {

            if (enrollmentId == null) {

                throw new IllegalArgumentException(
                        "Enrollment ID is required"
                );
            }

            if (amount == null) {

                throw new IllegalArgumentException(
                        "Payment amount is required"
                );
            }

            if (paymentMethod == null) {

                throw new IllegalArgumentException(
                        "Payment method is required"
                );
            }

            if (paymentReference == null
                    || paymentReference.isBlank()) {

                throw new IllegalArgumentException(
                        "Payment reference is required"
                );
            }

            return new PaymentCommand(this);
        }
    }
}