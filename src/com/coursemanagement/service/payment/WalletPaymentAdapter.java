package com.coursemanagement.service.payment;

import com.coursemanagement.service.payment.command.PaymentCommand;

public class WalletPaymentAdapter implements PaymentGateway {

    @Override
    public boolean pay(PaymentCommand command) {

        System.out.println(
                "Processing WALLET payment through Wallet Provider..."
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