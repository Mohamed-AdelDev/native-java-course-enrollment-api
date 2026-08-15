package com.coursemanagement.service.payment;

import com.coursemanagement.model.enums.PaymentMethod;

public class PaymentGatewayFactory {

    public static PaymentGateway getGateway(
            PaymentMethod paymentMethod) {

        if (paymentMethod == null) {

            throw new IllegalArgumentException(
                    "Payment method is required"
            );
        }

        return switch (paymentMethod) {

            case CARD ->
                    new CardPaymentAdapter();

            case WALLET ->
                    new WalletPaymentAdapter();

            case BANK_TRANSFER ->
                    new BankTransferPaymentAdapter();
        };
    }
}