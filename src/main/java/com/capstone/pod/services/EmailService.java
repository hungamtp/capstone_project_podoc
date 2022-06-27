package com.capstone.pod.services;

import com.capstone.pod.entities.Credential;

import javax.mail.MessagingException;

public interface EmailService {
    public void sendEmail() throws MessagingException;
    public void confirm(String email);
}