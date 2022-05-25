package com.capstone.pod.auth;

import com.capstone.pod.constant.user.UserErrorMessage;
import com.capstone.pod.entities.Credential;
import com.capstone.pod.exceptions.EmailNotFoundException;
import com.capstone.pod.repositories.CredentialRepository;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Data
@RequiredArgsConstructor
@Builder
public class ApplicationUserService implements UserDetailsService {
    private final CredentialRepository credentialRepository;
    @Override
    public UserDetails loadUserByUsername(String email){
        Optional<Credential> credential = credentialRepository.findCredentialByEmail(email);
        if (!credential.isPresent()) {
            throw new EmailNotFoundException(UserErrorMessage.EMAIL_NOT_FOUND);
        }
        return new UserDetail(credential.get());
    }
}
