package com.capstone.pod.services.implement;

import com.capstone.pod.constant.common.CommonMessage;
import com.capstone.pod.constant.credential.CredentialErrorMessage;
import com.capstone.pod.constant.role.RoleErrorMessage;
import com.capstone.pod.constant.role.RoleName;
import com.capstone.pod.constant.user.UserErrorMessage;
import com.capstone.pod.dto.factory.AddFactoryDto;
import com.capstone.pod.dto.factory.AddFactoryResponse;
import com.capstone.pod.dto.factory.FactoryPageResponseDto;
import com.capstone.pod.dto.user.UpdateAvatarDto;
import com.capstone.pod.dto.user.UpdatePasswordDto;
import com.capstone.pod.dto.user.UserDto;
import com.capstone.pod.entities.Credential;
import com.capstone.pod.entities.Factory;
import com.capstone.pod.entities.Role;
import com.capstone.pod.exceptions.*;
import com.capstone.pod.repositories.CredentialRepository;
import com.capstone.pod.repositories.FactoryRepository;
import com.capstone.pod.repositories.RoleRepository;
import com.capstone.pod.services.FactoryService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FactoryServiceImplement implements FactoryService {
    private final FactoryRepository factoryRepository;
    private final CredentialRepository credentialRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private  Credential getPermittedCredential(int credentialId) {
        Credential credential = credentialRepository.findById(credentialId)
                .orElseThrow(() -> new CredentialNotFoundException(CredentialErrorMessage.CREDENTIAL_NOT_FOUND_EXCEPTION));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Integer currentCredentialId = (Integer)authentication.getCredentials();
        if(!currentCredentialId.equals(credential.getId())){
            throw new PermissionException(CommonMessage.PERMISSION_EXCEPTION);
        }
        return credential;
    }

    @Override
    public Page<FactoryPageResponseDto> getAllFactories(Pageable pageable) {
        List<FactoryPageResponseDto> credentialFactories= credentialRepository.findAll(pageable).stream().filter(credential -> credential.getRole().getName().equals(RoleName.ROLE_FACTORY))
                .map(credential -> FactoryPageResponseDto.builder().email(credential.getEmail())
                        .id(credential.getFactory().getId())
                        .credentialId(credential.getId())
                        .address(credential.getAddress())
                        .phone(credential.getPhone())
                        .image(credential.getImage())
                        .name(credential.getFactory().getName())
                        .location(credential.getFactory().getLocation())
                        .isCollaborating(credential.getFactory().isCollaborating()).build()).collect(Collectors.toList());
        Page<FactoryPageResponseDto> pageReturn = new PageImpl(credentialFactories,pageable,credentialFactories.size());
        return pageReturn;
    }

    @Override
    public AddFactoryResponse addFactory(AddFactoryDto factoryDto) {
        Optional<Credential> credentialOptional = credentialRepository.findCredentialByEmail(factoryDto.getEmail());
        if (credentialOptional.isPresent()) {
            throw new UserNameExistException(UserErrorMessage.EMAIL_EXIST);
        }
        Factory factory = Factory.builder()
                .isCollaborating(true)
                .build();
        Role role = roleRepository.findByName(RoleName.ROLE_FACTORY)
                .orElseThrow(() -> new RoleNotFoundException(RoleErrorMessage.ROLE_NOT_FOUND));
        Credential credential = Credential.builder()
                .email(factoryDto.getEmail())
                .address(factoryDto.getAddress())
                .phone(factoryDto.getPhone())
                .role(role)
                .factory(factory)
                .password(passwordEncoder.encode(factoryDto.getPassword())).build();
        factoryRepository.save(factory);
        Credential credentialInrepo = credentialRepository.save(credential);
        AddFactoryResponse addFactoryDto = modelMapper.map(credentialInrepo, AddFactoryResponse.class);
        return addFactoryDto;
    }
    @Override
    public UserDto updatePassword(UpdatePasswordDto user, int credentialId) {
        Credential userRepo =  getPermittedCredential(credentialId);
        if(!passwordEncoder.matches(user.getOldPassword(),userRepo.getPassword())){
            throw new PasswordNotMatchException(UserErrorMessage.PASSWORD_DOES_NOT_MATCH);
        }
        userRepo.setPassword(passwordEncoder.encode(user.getNewPassword()));
        return modelMapper.map(credentialRepository.save(userRepo),UserDto.class);
    }
    @Override
    public UserDto updateAvatar(UpdateAvatarDto avatar, int userId) {
        Credential credential =  getPermittedCredential(userId);
        credential.setImage(avatar.getImage());
        return modelMapper.map(credentialRepository.save(credential),UserDto.class);
    }
}
