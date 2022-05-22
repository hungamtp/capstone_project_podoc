package com.capstone.pod.services.implement;

import com.capstone.pod.constant.common.CommonMessage;
import com.capstone.pod.constant.role.RoleErrorMessage;
import com.capstone.pod.constant.role.RoleName;
import com.capstone.pod.dto.auth.LoginDto;
import com.capstone.pod.dto.auth.LoginResponseDto;
import com.capstone.pod.dto.auth.RegisterResponseDto;
import com.capstone.pod.dto.user.*;
import com.capstone.pod.entities.Role;
import com.capstone.pod.entities.User;
import com.capstone.pod.exceptions.*;
import com.capstone.pod.jwt.JwtConfig;
import com.capstone.pod.constant.user.UserErrorMessage;
import com.capstone.pod.constant.user.UserStatus;
import com.capstone.pod.repositories.RoleRepository;
import com.capstone.pod.repositories.UserRepository;
import com.capstone.pod.services.UserService;
import com.capstone.pod.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImplement implements UserService {
    private final UserRepository userRepository;
    private final SecretKey secretKey;
    private final JwtConfig jwtConfig;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;

    public UserDto findByEmail(String email) {
        User user = userRepository.findUserByEmail(email).orElseThrow(() -> new EmailNotFoundException(UserErrorMessage.EMAIL_NOT_FOUND));
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public RegisterResponseDto register(RegisterUserDto user){
       Optional<User> optionalUser= userRepository.findUserByEmail(user.getEmail());
        if(optionalUser.isPresent()){
            throw new EmailExistException(UserErrorMessage.EMAIL_EXIST);
        }
        Role role = roleRepository.findByName(RoleName.ROLE_USER).orElseThrow(() -> new com.capstone.pod.exceptions.RoleNotFoundException(RoleErrorMessage.ROLE_NOT_FOUND));
        User userTmp = User.builder()
                .email(user.getEmail()).firstName(user.getFirstName()).lastName(user.getLastName()).role(role).avatar(user.getAvatar()).phone(user.getPhone())
                .password(passwordEncoder.encode(user.getPassword())).status(UserStatus.ACTIVE).isActive(true).isMailVerified(false).address(user.getAddress()).build();
        userRepository.save(userTmp);
        RegisterResponseDto registerResponseDto = modelMapper.map(userTmp,RegisterResponseDto.class);
        return registerResponseDto;
    }
    @Override
    public LoginResponseDto login(LoginDto user) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword());
        LoginResponseDto loginResponseDTO = null;
        Authentication authenticate = authenticationManager.authenticate(authentication);
        if (authenticate.isAuthenticated()) {
            Optional<User> userAuthenticatedOptional = userRepository.findUserByEmail(user.getEmail());
            User userAuthenticated = userAuthenticatedOptional.get();
            if (!userAuthenticated.getStatus().equals(UserStatus.ACTIVE)) {
                throw new UserDisableException(UserErrorMessage.USER_UNAVAILABLE);
            }
            String token = Utils.buildJWT(authenticate, userAuthenticated, secretKey, jwtConfig);
            loginResponseDTO = LoginResponseDto.builder()
                    .userId(userAuthenticated.getId())
                    .firstName(userAuthenticated.getFirstName())
                    .lastName(userAuthenticated.getLastName())
                    .email(userAuthenticated.getEmail())
                    .phone(userAuthenticated.getPhone())
                    .address(userAuthenticated.getAddress())
                    .avatar(userAuthenticated.getAvatar())
                    .roleName(userAuthenticated.getRole().getName())
                    .token(jwtConfig.getTokenPrefix() + token).build();
        }
        return loginResponseDTO;
    }

    @Override
    public UserDto deleteUserById(int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(UserErrorMessage.USER_NOT_FOUND));
        user.setStatus(UserStatus.INACTIVE);
        user.setActive(false);
        userRepository.save(user);
        UserDto userDto = modelMapper.map(user, UserDto.class);
        return userDto;
    }
    @Override
    public Page<UserDto> getAllUser(int pageNum, int pageSize) {
        Pageable pageable = PageRequest.of(pageNum, pageSize);
        Page<User> pageUser = userRepository.findAll(pageable);
        Page<UserDto> pageUserDTO = pageUser.map(user -> modelMapper.map(user, UserDto.class));
        return pageUserDTO;
    }

    @Override
    public UserDto getUserById(int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(UserErrorMessage.USER_NOT_FOUND));
        UserDto userDto = modelMapper.map(user, UserDto.class);
        return userDto;
    }

    @Override
    public UserDto addUser(AddUserDto user)
    throws RoleNotFoundException{
        Optional<User> userTmp = userRepository.findUserByEmail(user.getEmail());
        if (userTmp.isPresent()) {
            throw new UserNameExistException(UserErrorMessage.EMAIL_EXIST);
        }
        Role role =roleRepository.findByName(user.getRoleName()).orElseThrow(
                () -> new RoleNotFoundException(RoleErrorMessage.ROLE_NOT_FOUND));
        User userBuild = User.builder()
                .status(UserStatus.ACTIVE)
                .isActive(true)
                .address(user.getAddress())
                .role(role)
                .phone(user.getPhone())
                .email(user.getEmail())
                .avatar(user.getAvatar())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .password(passwordEncoder.encode(user.getPassword()))
                .build();
        User inRepo = userRepository.save(userBuild);
        UserDto userDTO = modelMapper.map(inRepo, UserDto.class);
        return userDTO;
    }
    @Override
    public UserDto updateUser(UpdateUserDto user,int userId) {
        User userRepo = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(UserErrorMessage.USER_NOT_FOUND));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = (String) authentication.getCredentials();
        if(email.equals(userRepo.getEmail())){
            throw new PermissionException(CommonMessage.PERMISSION_EXCEPTION);
        }
        Role role = roleRepository.findByName(user.getRoleName())
                .orElseThrow(() -> new UserNotFoundException(RoleErrorMessage.ROLE_NOT_FOUND));
        userRepo.setFirstName(user.getFirstName());
        userRepo.setLastName(user.getLastName());
        userRepo.setAddress(user.getAddress());
        userRepo.setPhone(user.getPhone());
        userRepo.setRole(role);
        return modelMapper.map(userRepository.save(userRepo),UserDto.class);
    }
    @Override
    public UserDto updateUserByAdmin(UpdateUserDtoByAdmin user, int userId) {
        User userRepo = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(UserErrorMessage.USER_NOT_FOUND));
        Role roleRepo = roleRepository.findByName(user.getRoleName())
                .orElseThrow(() -> new UserNotFoundException(RoleErrorMessage.ROLE_NOT_FOUND));
        userRepo.setFirstName(user.getFirstName());
        userRepo.setLastName(user.getLastName());
        userRepo.setAddress(user.getAddress());
        userRepo.setPhone(user.getPhone());
        userRepo.setStatus(user.getStatus());
        userRepo.setRole(roleRepo);
        return modelMapper.map(userRepository.save(userRepo),UserDto.class);
    }
}
