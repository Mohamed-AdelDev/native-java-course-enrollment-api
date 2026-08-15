package com.coursemanagement.service.payment;

import com.coursemanagement.service.payment.command.PaymentCommand;

public class CardPaymentAdapter implements PaymentGateway {

    @Override
    public boolean pay(PaymentCommand command) {

        System.out.println(
                "Processing CARD payment through Card Provider..."
        );

        System.out.println(
                "Reference: " + command.getPaymentReference()
        );

        System.out.println(
                "Amount: " + command.getAmount()
        );

        return true;
    }
}