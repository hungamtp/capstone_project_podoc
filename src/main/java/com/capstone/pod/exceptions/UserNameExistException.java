package com.capstone.pod.exceptions;

public class UserNameExistException extends RuntimeException{
    public UserNameExistException(String message) {
        super(message);
    }
}
