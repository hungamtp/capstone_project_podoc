package com.capstone.pod.services.implement;

import com.capstone.pod.constant.common.CommonMessage;
import com.capstone.pod.entities.Credential;
import com.capstone.pod.entities.VerificationToken;
import com.capstone.pod.exceptions.CredentialNotFoundException;
import com.capstone.pod.repositories.VerificationRepository;
import com.capstone.pod.services.VerificationTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Calendar;

@Service
@RequiredArgsConstructor
public class VerificationTokenServiceImplement implements VerificationTokenService {
    private final VerificationRepository verificationRepository;
    @Override
    public VerificationToken findByToken(String token) {
        return verificationRepository.findByToken(token).orElseThrow(()->new CredentialNotFoundException(CommonMessage.VERIFICATION_TOKEN_EXCEPTION));
    }

    @Override
    public VerificationToken findByCredential(Credential credential) {
        return verificationRepository.findByCredential(credential).orElseThrow(()->new CredentialNotFoundException(CommonMessage.VERIFICATION_TOKEN_EXCEPTION));
    }

    @Override
    public void save(Credential credential, String token) {
        //set expiry date to 24 hours
        VerificationToken verificationToken = VerificationToken.builder().expiryDate(calculateExpiryTime(24*60)).token(token).credential(credential).build();
         verificationRepository.save(verificationToken);
    }

    private Timestamp calculateExpiryTime(int expiryTimeInMinutes){
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, expiryTimeInMinutes);
        return new Timestamp(cal.getTime().getTime());
    }
}
