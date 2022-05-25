package com.capstone.pod.services;

import com.capstone.pod.dto.auth.LoginDto;
import com.capstone.pod.dto.auth.LoginResponseDto;
import com.capstone.pod.dto.auth.RegisterResponseDto;
import com.capstone.pod.dto.auth.RegisterDto;
import com.capstone.pod.dto.credential.CredentialDto;
import com.capstone.pod.dto.user.UserDto;

import javax.management.relation.RoleNotFoundException;

public interface AuthService {
    public RegisterResponseDto register(RegisterDto user) throws RoleNotFoundException;
    public LoginResponseDto login(LoginDto user);
    public CredentialDto findCredentialByEmail(String email);
}
