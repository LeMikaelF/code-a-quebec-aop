package com.mikaelfrancoeur.aoptalk.decorator;

interface PaymentService {

    void pay() ;

    static PaymentService create() {
        return new AuditingPaymentService(new DefaultPaymentService());
    }
}
