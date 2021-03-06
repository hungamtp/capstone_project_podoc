package com.capstone.pod.services;

import com.capstone.pod.entities.Credential;

import javax.mail.MessagingException;

public interface EmailService {
    public void sendEmail();
    public void sendEmailToGetBackPassword(String email);
    public void confirm(String email, String token);
    public void resetPassword(String email, String token, String password);
}
