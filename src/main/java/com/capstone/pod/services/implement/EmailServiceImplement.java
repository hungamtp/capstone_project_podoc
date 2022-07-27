package com.capstone.pod.services.implement;

import com.capstone.pod.constant.common.CommonMessage;
import com.capstone.pod.constant.credential.CredentialErrorMessage;
import com.capstone.pod.entities.Credential;
import com.capstone.pod.entities.VerificationToken;
import com.capstone.pod.exceptions.CredentialNotFoundException;
import com.capstone.pod.exceptions.PermissionException;
import com.capstone.pod.repositories.CredentialRepository;
import com.capstone.pod.repositories.VerificationRepository;
import com.capstone.pod.services.EmailService;
import com.capstone.pod.services.VerificationTokenService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmailServiceImplement implements EmailService {
    private final CredentialRepository credentialRepository;
    private final VerificationTokenService verificationTokenService;
    private final VerificationRepository verificationRepository;
    private final JavaMailSender javaMailSender;
    private final PasswordEncoder passwordEncoder;

    private Timestamp calculateExpiryTime(int expiryTimeInMinutes){
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, expiryTimeInMinutes);
        return new Timestamp(cal.getTime().getTime());
    }

    @SneakyThrows
    @Override
    public void sendEmail(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentCredentialId = (String)authentication.getCredentials();
        Credential credential = credentialRepository.findById(currentCredentialId)
                .orElseThrow(() -> new CredentialNotFoundException(CredentialErrorMessage.CREDENTIAL_NOT_FOUND_EXCEPTION));
        String token = UUID.randomUUID().toString();
        Optional<VerificationToken> verificationToken = verificationRepository.findByCredential(credential);
        if(verificationToken.isPresent()){
           verificationToken.get().setToken(token);
           verificationToken.get().setExpiryDate(calculateExpiryTime(24*60)); // verify email token 24hours expire
           verificationRepository.save(verificationToken.get());
        }
        else { verificationTokenService.save(credential,token, false);}
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage,true);
        mimeMessageHelper.setTo(credential.getEmail());
            mimeMessageHelper.setSubject("Thư xác thực email từ PODOC");
            mimeMessageHelper.setText("Cám ơn bạn vì đã xác thực email, vui lòng nhấn đường link bên dưới để email được xác thực  \n"+"http://podoc.store/email/confirm/"
                    +credential.getEmail()+"/"
                    +token
                    +"\nChúc bạn một ngày làm việc tốt lành, \n" +
                    "\n" +
                    "Trân Trọng, ");
        javaMailSender.send(mimeMessage);
    }

    @SneakyThrows
    @Override
    public void sendEmailToGetBackPassword(String email) {
        Credential credential = credentialRepository.findCredentialByEmail(email)
                .orElseThrow(() -> new CredentialNotFoundException(CredentialErrorMessage.CREDENTIAL_NOT_FOUND_EXCEPTION));
        String token = UUID.randomUUID().toString();
        Optional<VerificationToken> verificationToken = verificationRepository.findByCredential(credential);
        if(verificationToken.isPresent()){
            verificationToken.get().setToken(token);
            verificationToken.get().setExpiryDate(calculateExpiryTime(60));
            verificationRepository.save(verificationToken.get());
        }
        else { verificationTokenService.save(credential,token, true);}
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage,true);
        mimeMessageHelper.setTo(credential.getEmail());
        mimeMessageHelper.setSubject("Thư lấy lại mật khẩu từ PODOC");
        mimeMessageHelper.setText("Lấy lại mật khẩu thành công, vui lòng nhấn đường link bên dưới để đặt lại mật khẩu  \n"+"http://podoc.store/reset-password/"
                +credential.getEmail()+"/"
                +token
                +"\nChúc bạn một ngày làm việc tốt lành, \n" +
                "\n" +
                "Trân Trọng, ");
        javaMailSender.send(mimeMessage);
    }

    @Override
    public void confirm(String email, String token) {
        Credential credential = credentialRepository.findCredentialByEmail(email).orElseThrow(() -> new CredentialNotFoundException(CredentialErrorMessage.CREDENTIAL_NOT_FOUND_EXCEPTION));
        VerificationToken verificationToken = verificationTokenService.findByToken(token);
        Timestamp currentTimeStamp = new Timestamp(System.currentTimeMillis());
        if(verificationToken.getExpiryDate().before(currentTimeStamp)){
            throw new PermissionException(CommonMessage.TOKEN_EXPIRED_EXCEPTION);
        }
        credential.setMailVerified(true);
        credentialRepository.save(credential);
    }

    @Override
    public void resetPassword(String email, String token, String password) {
        Credential credential = credentialRepository.findCredentialByEmail(email).orElseThrow(() -> new CredentialNotFoundException(CredentialErrorMessage.CREDENTIAL_NOT_FOUND_EXCEPTION));
        VerificationToken verificationToken = verificationTokenService.findByToken(token);
        Timestamp currentTimeStamp = new Timestamp(System.currentTimeMillis());
        if(verificationToken.getExpiryDate().before(currentTimeStamp)){
            throw new PermissionException(CommonMessage.TOKEN_EXPIRED_EXCEPTION);
        }
        credential.setPassword(passwordEncoder.encode(password));
        credentialRepository.save(credential);
    }
}
