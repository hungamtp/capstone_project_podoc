package com.capstone.pod.services.implement;

import com.capstone.pod.constant.credential.CredentialErrorMessage;
import com.capstone.pod.entities.Credential;
import com.capstone.pod.entities.VerificationToken;
import com.capstone.pod.exceptions.CredentialNotFoundException;
import com.capstone.pod.repositories.CredentialRepository;
import com.capstone.pod.services.EmailService;
import com.capstone.pod.services.VerificationTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmailServiceImplement implements EmailService {
    private final CredentialRepository credentialRepository;
    private final VerificationTokenService verificationTokenService;
    private final JavaMailSender javaMailSender;

    @Override
    public void sendEmail() throws MessagingException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Integer currentCredentialId = (Integer)authentication.getCredentials();
        Credential credential = credentialRepository.findById(currentCredentialId)
                .orElseThrow(() -> new CredentialNotFoundException(CredentialErrorMessage.CREDENTIAL_NOT_FOUND_EXCEPTION));
        String token = UUID.randomUUID().toString();
        verificationTokenService.save(credential,token);
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage,true);
        mimeMessageHelper.setTo(credential.getEmail());
        mimeMessageHelper.setSubject("Email address verification");
        mimeMessageHelper.setText("Please click this link to verify your email"+" http://localhost:8080/auth/confirm/"+credential.getEmail()+"/"+token);
        javaMailSender.send(mimeMessage);
    }

    @Override
    public void confirm(String email) {
        Credential credential = credentialRepository.findCredentialByEmail(email).orElseThrow(() -> new CredentialNotFoundException(CredentialErrorMessage.CREDENTIAL_NOT_FOUND_EXCEPTION));
        credential.setMailVerified(true);
        credentialRepository.save(credential);
    }
}
