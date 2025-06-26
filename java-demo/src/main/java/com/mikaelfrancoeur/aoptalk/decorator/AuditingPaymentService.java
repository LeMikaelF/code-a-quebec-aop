package com.mikaelfrancoeur.aoptalk.decorator;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class AuditingPaymentService implements PaymentService {

    private final PaymentService delegate;

    @Override
    public void pay() {
        delegate.pay();

        System.out.println("auditing payment");
    }
}
