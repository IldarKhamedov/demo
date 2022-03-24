package com.example.demo;

import org.springframework.stereotype.Component;

@Component
public class Calculator {

    public int sum(int a, int b) {
        return a + b;
    }

    public int multiply(int a, int b) {
        return a * b;
    }

}
