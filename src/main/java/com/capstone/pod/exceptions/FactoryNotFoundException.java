package com.capstone.pod.exceptions;

public class FactoryNotFoundException extends RuntimeException {
    public FactoryNotFoundException(String message) {
        super(message);
    }
}
