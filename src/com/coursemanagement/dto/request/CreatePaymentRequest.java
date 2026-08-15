package com.coursemanagement.dto.request;

public class CreatePaymentRequest {

    private String paymentMethod;
    private String paymentReference;

    public CreatePaymentRequest(
            String paymentMethod,
            String paymentReference) {

        this.paymentMethod =
                paymentMethod;

        this.paymentReference =
                paymentReference;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public String getPaymentReference() {
        return paymentReference;
    }
}