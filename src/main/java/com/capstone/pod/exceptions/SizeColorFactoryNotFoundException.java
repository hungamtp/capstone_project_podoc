package com.capstone.pod.exceptions;

import lombok.AllArgsConstructor;

import javax.persistence.EntityNotFoundException;


public class SizeColorFactoryNotFoundException extends EntityNotFoundException {
    public SizeColorFactoryNotFoundException(String message) {
        super(message);
    }
}
