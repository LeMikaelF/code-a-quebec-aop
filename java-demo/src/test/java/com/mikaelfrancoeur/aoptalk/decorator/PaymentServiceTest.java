package com.mikaelfrancoeur.aoptalk.decorator;

import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

class PaymentServiceTest implements WithAssertions {

    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    @BeforeEach
    void beforeEach() {
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @AfterEach
    void afterEach() {
        System.setOut(standardOut);
    }

    @Test
    void test() {
        PaymentService paymentService = PaymentService.create();

        paymentService.pay();

        assertThat(outputStreamCaptor.toString()).containsSubsequence("processing payment", "auditing payment");
    }

}