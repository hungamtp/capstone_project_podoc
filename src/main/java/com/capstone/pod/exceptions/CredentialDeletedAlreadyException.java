package com.capstone.pod.exceptions;

public class CredentialDeletedAlreadyException extends RuntimeException{
    public CredentialDeletedAlreadyException(String message){
        super(message);
    }
}
