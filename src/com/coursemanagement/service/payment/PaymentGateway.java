package com.coursemanagement.service.payment;

import com.coursemanagement.service.payment.command.PaymentCommand;

public interface PaymentGateway {

    boolean pay(PaymentCommand command);
}