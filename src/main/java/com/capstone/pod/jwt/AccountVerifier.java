package com.capstone.pod.jwt;

import com.capstone.pod.constant.credential.CredentialErrorMessage;
import com.capstone.pod.constant.user.UserErrorMessage;
import com.capstone.pod.entities.Credential;
import com.capstone.pod.exceptions.CredentialNotFoundException;
import com.capstone.pod.exceptions.PermissionException;
import com.capstone.pod.repositories.CredentialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class AccountVerifier extends OncePerRequestFilter {
    private  final CredentialRepository credentialRepository;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentCredentialId = (String) authentication.getCredentials();
        Credential credential = credentialRepository.findById(currentCredentialId).orElseThrow(() -> new CredentialNotFoundException(CredentialErrorMessage.CREDENTIAL_NOT_FOUND_EXCEPTION));
        if(!credential.getUser().getStatus().equals("ACTIVE")) throw new PermissionException(UserErrorMessage.USER_UNAVAILABLE);
        filterChain.doFilter(request, response);
    }
}
