package com.capstone.pod.services.implement;

import com.capstone.pod.constant.role.RoleErrorMessage;
import com.capstone.pod.constant.role.RoleName;
import com.capstone.pod.constant.user.UserErrorMessage;
import com.capstone.pod.constant.user.UserStatus;
import com.capstone.pod.constant.validation_message.ValidationMessage;
import com.capstone.pod.dto.auth.LoginDto;
import com.capstone.pod.dto.auth.LoginResponseDto;
import com.capstone.pod.dto.auth.RegisterDto;
import com.capstone.pod.dto.auth.RegisterResponseDto;
import com.capstone.pod.dto.credential.CredentialDto;
import com.capstone.pod.entities.Credential;
import com.capstone.pod.entities.Role;
import com.capstone.pod.entities.User;
import com.capstone.pod.exceptions.*;
import com.capstone.pod.jwt.JwtConfig;
import com.capstone.pod.repositories.CredentialRepository;
import com.capstone.pod.repositories.RoleRepository;
import com.capstone.pod.repositories.UserRepository;
import com.capstone.pod.services.AuthService;
import com.capstone.pod.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImplement implements AuthService {
    private final CredentialRepository credentialRepository;
    private final RoleRepository roleRepository;
    private final SecretKey secretKey;
    private final JwtConfig jwtConfig;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
        @Override
    public RegisterResponseDto register(RegisterDto registerDto){
       Optional<Credential> optionalUser= credentialRepository.findCredentialByEmail(registerDto.getEmail());
        if(optionalUser.isPresent()){
            throw new EmailExistException(UserErrorMessage.EMAIL_EXIST);
        }
       try {
           Integer.parseInt(registerDto.getPhone());
       }
       catch (NumberFormatException e){
           throw new NumberFormatException(ValidationMessage.PHONE_FORMAT_VALID_MESSAGE);
       }
        User user = User.builder().lastName(registerDto.getLastName()).firstName(registerDto.getFirstName()).status(UserStatus.ACTIVE).build();
        Role role = roleRepository.findByName(RoleName.ROLE_USER).orElseThrow(() -> new RoleNotFoundException(RoleErrorMessage.ROLE_NOT_FOUND));
        Credential credential = Credential.builder()
                .email(registerDto.getEmail())
                .phone(registerDto.getPhone())
                .role(role)
                .user(user)
                .password(passwordEncoder.encode(registerDto.getPassword())).build();
        userRepository.save(user);
        Credential credentialInRepo =  credentialRepository.save(credential);
        RegisterResponseDto registerResponseDto = modelMapper.map(credentialInRepo,RegisterResponseDto.class);
        return registerResponseDto;
    }
    @Override
    public LoginResponseDto login(LoginDto credential) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(credential.getEmail(), credential.getPassword());
        LoginResponseDto loginResponseDTO = null;
        Authentication authenticate = authenticationManager.authenticate(authentication);
        if (authenticate.isAuthenticated()) {
            Optional<Credential> credentialAuthenticatedOptional = credentialRepository.findCredentialByEmail(credential.getEmail());
            Credential credentialAuthenticated = credentialAuthenticatedOptional.get();
            if(credentialAuthenticated.getUser() != null ){
            if (!credentialAuthenticated.getUser().getStatus().equals(UserStatus.ACTIVE)) {
                throw new UserDisableException(UserErrorMessage.USER_UNAVAILABLE);
            }}
            String token = Utils.buildJWT(authenticate, credentialAuthenticated, secretKey, jwtConfig);
            loginResponseDTO = LoginResponseDto.builder()
                    .credentialId(credentialAuthenticated.getId())
                    .email(credentialAuthenticated.getEmail())
                    .phone(credentialAuthenticated.getPhone())
                    .address(credentialAuthenticated.getAddress())
                    .roleName(credentialAuthenticated.getRole().getName())
                    .token(jwtConfig.getTokenPrefix()+" "+ token).build();
        }
        return loginResponseDTO;
    }
    @Override
    public CredentialDto findCredentialByEmail(String email) {
        Credential credential = credentialRepository.findCredentialByEmail(email).orElseThrow(() -> new EmailNotFoundException(UserErrorMessage.EMAIL_NOT_FOUND));
        return modelMapper.map(credential, CredentialDto.class);
    }
}
