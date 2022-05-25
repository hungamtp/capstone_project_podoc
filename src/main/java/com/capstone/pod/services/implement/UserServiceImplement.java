package com.capstone.pod.services.implement;

import com.capstone.pod.constant.common.CommonMessage;
import com.capstone.pod.constant.credential.CredentialErrorMessage;
import com.capstone.pod.constant.role.RoleErrorMessage;
import com.capstone.pod.constant.user.UserErrorMessage;
import com.capstone.pod.constant.user.UserStatus;
import com.capstone.pod.dto.credential.CredentialDto;
import com.capstone.pod.dto.user.*;
import com.capstone.pod.entities.Credential;
import com.capstone.pod.entities.Role;
import com.capstone.pod.entities.User;
import com.capstone.pod.exceptions.CredentialDeletedAlreadyException;
import com.capstone.pod.exceptions.CredentialNotFoundException;
import com.capstone.pod.exceptions.PermissionException;
import com.capstone.pod.exceptions.UserNotFoundException;
import com.capstone.pod.repositories.CredentialRepository;
import com.capstone.pod.repositories.RoleRepository;
import com.capstone.pod.repositories.UserRepository;
import com.capstone.pod.services.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImplement implements UserService {
    private final UserRepository userRepository;
    private final CredentialRepository credentialRepository;

    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;
    private Credential getPermittedCredential(int credentialId) {
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
    public UserDto deleteUserById(int credentialId) {
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
        Page<UserDto> pageUserDTO = credentials.map(user -> modelMapper.map(user, UserDto.class));
        return pageUserDTO;
    }

    @Override
    public UserDto getUserById(int userId) {
        Credential credential = getPermittedCredential(userId);
        UserDto userDto = modelMapper.map(credential, UserDto.class);
        return userDto;
    }
    @Override
    public UserDto getUserByIdRoleAdmin(int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(UserErrorMessage.USER_NOT_FOUND));
        UserDto userDto = modelMapper.map(user, UserDto.class);
        return userDto;
    }

    @Override
    public UserDto addUser(AddUserDto user) {
        return null;
    }

    //    @Override
//    public UserDto addUser(AddUserDto user)
//    throws RoleNotFoundException{
//        Optional<User> userTmp = userRepository.findUserByEmail(user.getEmail());
//        if (userTmp.isPresent()) {
//            throw new UserNameExistException(UserErrorMessage.EMAIL_EXIST);
//        }
//        Role role =roleRepository.findByName(user.getRoleName()).orElseThrow(
//                () -> new RoleNotFoundException(RoleErrorMessage.ROLE_NOT_FOUND));
//        User userBuild = User.builder()
//                .status(UserStatus.ACTIVE)
//                .isActive(true)
//                .address(user.getAddress())
//                .role(role)
//                .phone(user.getPhone())
//                .email(user.getEmail())
//                .avatar(user.getAvatar())
//                .firstName(user.getFirstName())
//                .lastName(user.getLastName())
//                .password(passwordEncoder.encode(user.getPassword()))
//                .build();
//        User inRepo = userRepository.save(userBuild);
//        UserDto userDTO = modelMapper.map(inRepo, UserDto.class);
//        return userDTO;
//    }
    @Override
    public UserDto updateUser(UpdateUserDto user,int userId) {
       Credential userRepo =  getPermittedCredential(userId);
        return modelMapper.map(credentialRepository.save(userRepo),UserDto.class);
    }
    @Override
    public UserDto updateUserByAdmin(UpdateUserDtoByAdmin user, int userId) {
        User userRepo = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(UserErrorMessage.USER_NOT_FOUND));
        Role roleRepo = roleRepository.findByName(user.getRoleName())
                .orElseThrow(() -> new UserNotFoundException(RoleErrorMessage.ROLE_NOT_FOUND));
        userRepo.setFirstName(user.getFirstName());
        userRepo.setLastName(user.getLastName());
        userRepo.setStatus(user.getStatus());
//        userRepo.setRole(roleRepo);
        return modelMapper.map(userRepository.save(userRepo),UserDto.class);
    }

    @Override
    public UserDto updatePassword(UpdatePasswordDto user, int userId) {
        Credential userRepo =  getPermittedCredential(userId);
//        userRepo.setPassword(passwordEncoder.encode(user.getPassword()));
        return modelMapper.map(credentialRepository.save(userRepo),UserDto.class);
    }
    @Override
    public UserDto updateEmail(UpdateEmailDto user, int userId) {
        Credential userRepo =  getPermittedCredential(userId);
        userRepo.setEmail(user.getEmail());
        return modelMapper.map(credentialRepository.save(userRepo),UserDto.class);
    }
}
