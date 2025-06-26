package com.mikaelfrancoeur.aoptalk.decorator;

public class DefaultPaymentService implements PaymentService {

    @Override
    public void pay() {
        System.out.println("processing payment");
    }
}
