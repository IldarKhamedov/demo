package com.example.demo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CalculatorTest { //unit test

    private final Calculator calculator = new Calculator();

    @Test
    public void testSum() {
        assertEquals(4, calculator.sum(2, 2));
        assertEquals(-2, calculator.sum(-4, 2));
    }

}
