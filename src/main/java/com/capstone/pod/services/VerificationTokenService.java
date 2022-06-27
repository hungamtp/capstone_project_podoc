package com.capstone.pod.services;

import com.capstone.pod.entities.Credential;
import com.capstone.pod.entities.VerificationToken;

public interface VerificationTokenService {
    public VerificationToken findByToken(String token);
    public VerificationToken findByCredential(Credential credential);
    public void save(Credential credential, String token);
}
