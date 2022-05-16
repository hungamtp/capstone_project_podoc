package com.capstone.pod.exceptions;

public class UsernameOrPasswordNotFoundException extends RuntimeException{
    public UsernameOrPasswordNotFoundException(String message) {
        super(message);
    }
}
