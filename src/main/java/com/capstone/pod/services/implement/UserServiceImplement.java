package com.capstone.pod.services.implement;

import com.capstone.pod.constant.common.CommonMessage;
import com.capstone.pod.constant.credential.CredentialErrorMessage;
import com.capstone.pod.constant.role.RoleErrorMessage;
import com.capstone.pod.constant.role.RoleName;
import com.capstone.pod.constant.user.UserErrorMessage;
import com.capstone.pod.constant.user.UserStatus;
import com.capstone.pod.dto.user.*;
import com.capstone.pod.entities.Credential;
import com.capstone.pod.entities.Role;
import com.capstone.pod.entities.User;
import com.capstone.pod.exceptions.*;
import com.capstone.pod.repositories.CredentialRepository;
import com.capstone.pod.repositories.RoleRepository;
import com.capstone.pod.repositories.UserRepository;
import com.capstone.pod.services.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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
public class UserServiceImplement implements UserService {
    private final UserRepository userRepository;
    private final CredentialRepository credentialRepository;
    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private  Credential getPermittedCredential(String credentialId) {
        Credential credential = credentialRepository.findById(credentialId)
                .orElseThrow(() -> new CredentialNotFoundException(CredentialErrorMessage.CREDENTIAL_NOT_FOUND_EXCEPTION));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentCredentialId = (String)authentication.getCredentials();
        if(!currentCredentialId.equals(credential.getId())){
            throw new PermissionException(CommonMessage.PERMISSION_EXCEPTION);
        }
        return credential;
    }
    @Override
    public UserDto deleteUserById(String credentialId) {
        Credential credential = credentialRepository.findById(credentialId)
                .orElseThrow(() -> new CredentialNotFoundException(CredentialErrorMessage.CREDENTIAL_NOT_FOUND_EXCEPTION));
        if(credential.getUser()!=null) {
            if(credential.getUser().getStatus().equals(UserStatus.INACTIVE)){
            throw new CredentialDeletedAlreadyException(CredentialErrorMessage.CREDENTIAL_DELETED_ALREADY);
            }
            credential.getUser().setStatus(UserStatus.INACTIVE);
            UserDto  userDto = modelMapper.map(credentialRepository.save(credential), UserDto.class);
            return userDto;
        }
        return null;
    }
    @Override
    public Page<UserDto> getAllUser(int pageNum, int pageSize) {
        Pageable pageable = PageRequest.of(pageNum, pageSize);
        Page<Credential> credentials = credentialRepository.findAll(pageable);
        List<UserDto> listUserDto = credentials.stream()
                .filter(credential -> credential.getRole().getName().equals(RoleName.ROLE_USER) || credential.getRole().getName().equals(RoleName.ROLE_ADMIN)).
                map(user -> modelMapper.map(user, UserDto.class)).collect(Collectors.toList());
        Page<UserDto> pageUserDTO = new PageImpl<>(listUserDto,pageable,listUserDto.size());
        return pageUserDTO;
    }
    @Override
    public Page<UserDto> getAllUserByName(int pageNum, int pageSize, String name) {
        Pageable pageable = PageRequest.of(pageNum, pageSize);
        Page<Credential> credentials = credentialRepository.findAllByUserFirstNameContains(pageable, name);
        List<UserDto> listUserDto = credentials.stream()
                .map(user -> modelMapper.map(user, UserDto.class)).collect(Collectors.toList());
        Page<UserDto> pageUserDTO = new PageImpl<>(listUserDto,pageable,credentials.getTotalElements());
        return pageUserDTO;
    }

    @Override
    public Page<UserDto> getAllByRoleName(int pageNum, int pageSize, String roleName) {
        Pageable pageable = PageRequest.of(pageNum, pageSize);
        Page<Credential> credentials = credentialRepository.findAllByRoleName(pageable, roleName);
        Page<UserDto> pageUserDTO = credentials.map(user -> modelMapper.map(user, UserDto.class));
        return pageUserDTO;
    }

    @Override
    public UserDto getUserById(String userId) {
        Credential credential = getPermittedCredential(userId);
        UserDto userDto = modelMapper.map(credential, UserDto.class);
        return userDto;
    }
    @Override
    public UserDto findByEmail(String email) {
        Credential credential = credentialRepository.findCredentialByEmailContains(email)
                .orElseThrow(() -> new UserNotFoundException(UserErrorMessage.USER_NOT_FOUND));
        UserDto userDto = modelMapper.map(credential, UserDto.class);
        return userDto;
    }

    @Override
    public UserDto getUserByIdRoleAdmin(String credentialId) {
        Credential credential = credentialRepository.findById(credentialId)
                .orElseThrow(() -> new UserNotFoundException(UserErrorMessage.USER_NOT_FOUND));
        UserDto userDto = modelMapper.map(credential, UserDto.class);
        return userDto;
    }

        @Override
    public UserDto addUser(AddUserDto user) {
        Optional<Credential> credentialOptional = credentialRepository.findCredentialByEmail(user.getEmail());
        if (credentialOptional.isPresent()) {
            throw new UserNameExistException(UserErrorMessage.EMAIL_EXIST);
        }
        User userBuild = User.builder()
                .status(UserStatus.ACTIVE)
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();
        Role role = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new RoleNotFoundException(RoleErrorMessage.ROLE_NOT_FOUND));
        Credential credential = Credential.builder()
                    .email(user.getEmail())
                    .address(user.getAddress())
                    .phone(user.getPhone())
                    .role(role)
                    .user(userBuild)
                    .password(passwordEncoder.encode(user.getPassword())).build();
        userRepository.save(userBuild);
        Credential credentialInrepo = credentialRepository.save(credential);
        UserDto userDTO = modelMapper.map(credentialInrepo, UserDto.class);
        return userDTO;
    }
    @Override
    public UserDto addAdmin(AddUserDto user) {
        Optional<Credential> credentialOptional = credentialRepository.findCredentialByEmail(user.getEmail());
        if (credentialOptional.isPresent()) {
            throw new UserNameExistException(UserErrorMessage.EMAIL_EXIST);
        }
        User userBuild = User.builder()
                .status(UserStatus.ACTIVE)
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();
        Role role = roleRepository.findByName(RoleName.ROLE_ADMIN)
                .orElseThrow(() -> new RoleNotFoundException(RoleErrorMessage.ROLE_NOT_FOUND));
        Credential credential = Credential.builder()
                    .email(user.getEmail())
                    .address(user.getAddress())
                    .phone(user.getPhone())
                    .role(role)
                    .user(userBuild)
                    .password(passwordEncoder.encode(user.getPassword())).build();
        userRepository.save(userBuild);
        Credential credentialInrepo = credentialRepository.save(credential);
        UserDto userDTO = modelMapper.map(credentialInrepo, UserDto.class);
        return userDTO;
    }
    @Override
    public UserDto addFactory(AddUserDto user) {
        Optional<Credential> credentialOptional = credentialRepository.findCredentialByEmail(user.getEmail());
        if (credentialOptional.isPresent()) {
            throw new UserNameExistException(UserErrorMessage.EMAIL_EXIST);
        }
        User userBuild = User.builder()
                .status(UserStatus.ACTIVE)
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();
        Role role = roleRepository.findByName(RoleName.ROLE_FACTORY)
                .orElseThrow(() -> new RoleNotFoundException(RoleErrorMessage.ROLE_NOT_FOUND));
        Credential credential = Credential.builder()
                    .email(user.getEmail())
                    .address(user.getAddress())
                    .phone(user.getPhone())
                    .role(role)
                    .user(userBuild)
                    .password(passwordEncoder.encode(user.getPassword())).build();
        userRepository.save(userBuild);
        Credential credentialInrepo = credentialRepository.save(credential);
        UserDto userDTO = modelMapper.map(credentialInrepo, UserDto.class);
        return userDTO;
    }
    @Override
    public UserDto updateUser(UpdateUserDto user,String credentialId) {
        Credential credential =  getPermittedCredential(credentialId);
        credential.setAddress(user.getAddress());
        credential.setPhone(user.getPhone());
        credential.getUser().setFirstName(user.getFirstName());
        credential.getUser().setLastName(user.getLastName());
        return modelMapper.map(credentialRepository.save(credential),UserDto.class);
    }
    @Override
    public UserDto updateUserByAdmin(UpdateUserDtoByAdmin user, String credentialId) {
        Credential credential = credentialRepository.findById(credentialId)
                .orElseThrow(() -> new UserNotFoundException(CredentialErrorMessage.CREDENTIAL_NOT_FOUND_EXCEPTION));
        Role role = roleRepository.findByName(user.getRoleName()).orElseThrow(() -> new RoleNotFoundException(RoleErrorMessage.ROLE_NOT_FOUND) );
        credential.setAddress(user.getAddress());
        credential.setPhone(user.getPhone());
        credential.setRole(role);
        credential.getUser().setFirstName(user.getFirstName());
        credential.getUser().setLastName(user.getLastName());
        return modelMapper.map(credentialRepository.save(credential),UserDto.class);
    }

    @Override
    public UserDto updatePassword(UpdatePasswordDto user, String credentialId) {
        Credential credential =  getPermittedCredential(credentialId);
        if(!passwordEncoder.matches(user.getOldPassword(),credential.getPassword())){
            throw new PasswordNotMatchException(UserErrorMessage.PASSWORD_DOES_NOT_MATCH);
        }
        credential.setPassword(passwordEncoder.encode(user.getNewPassword()));
        return modelMapper.map(credentialRepository.save(credential),UserDto.class);
    }

    @Override
    public UserDto updateAvatar(UpdateAvatarDto avatar, String userId) {
        Credential credential =  getPermittedCredential(userId);
        credential.setImage(avatar.getImage());
        return modelMapper.map(credentialRepository.save(credential),UserDto.class);
    }
}
