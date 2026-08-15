package com.coursemanagement.service.payment;

import com.coursemanagement.service.payment.command.PaymentCommand;

public class BankTransferPaymentAdapter
        implements PaymentGateway {

    @Override
    public boolean pay(PaymentCommand command) {

        System.out.println(
                "Processing BANK TRANSFER payment through Bank Provider..."
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