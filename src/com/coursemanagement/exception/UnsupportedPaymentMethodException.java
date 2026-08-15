package com.coursemanagement.exception;

public class UnsupportedPaymentMethodException
        extends RuntimeException {

    public UnsupportedPaymentMethodException(String message) {
        super(message);
    }
}