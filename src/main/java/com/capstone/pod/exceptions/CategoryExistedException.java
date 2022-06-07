package com.capstone.pod.exceptions;

public class CategoryExistedException extends RuntimeException {
    public CategoryExistedException(String message) {
        super(message);
    }
}
