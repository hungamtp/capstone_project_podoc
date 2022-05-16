package com.capstone.pod.exceptions;

public class ProductNameExistException extends RuntimeException{
    public ProductNameExistException(String message) {
        super(message);
    }
}
