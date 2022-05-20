package com.capstone.pod.services.implement;

import com.capstone.pod.constant.role.RoleErrorMessage;
import com.capstone.pod.constant.role.RoleName;
import com.capstone.pod.dto.auth.LoginDto;
import com.capstone.pod.dto.auth.LoginResponseDto;
import com.capstone.pod.dto.auth.RegisterResponseDto;
import com.capstone.pod.dto.user.AddUserDto;
import com.capstone.pod.dto.user.RegisterUserDto;
import com.capstone.pod.dto.user.UserDto;
import com.capstone.pod.entities.Role;
import com.capstone.pod.entities.User;
import com.capstone.pod.exceptions.*;
import com.capstone.pod.jwt.JwtConfig;
import com.capstone.pod.constant.user.UserErrorMessage;
import com.capstone.pod.constant.user.UserStatusMessage;
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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.LocalDate;
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
    @Override
    public UserDto findByUserName(String username) {
        Optional<User> user = Optional.ofNullable(userRepository.findUserByUsername(username).orElseThrow(() -> new UsernameNotFoundException(UserErrorMessage.USERNAME_NOT_FOUND)));
        return modelMapper.map(user, UserDto.class);
    }

    public UserDto findByEmail(String email) {
        Optional<User> user = Optional.ofNullable(userRepository.findUserByEmail(email).orElseThrow(() -> new EmailNotFoundException(UserErrorMessage.EMAIL_NOT_FOUND)));
        return modelMapper.map(user.get(), UserDto.class);
    }

    @Override
    public RegisterResponseDto register(RegisterUserDto user){
        Optional.ofNullable(userRepository.findUserByEmail(user.getEmail()).orElseThrow(() -> new UserNameExistException(UserErrorMessage.EMAIL_EXIST)));
        Optional<Role> role = Optional.ofNullable(roleRepository.findByName(RoleName.ROLE_USER).orElseThrow(() -> new com.capstone.pod.exceptions.RoleNotFoundException(RoleErrorMessage.ROLE_NOT_FOUND)));
        User userTmp = User.builder().username(user.getUsername())
                .email(user.getEmail()).firstName(user.getFirstName()).lastName(user.getLastName()).role(role.get()).avatar(user.getAvatar()).phone(user.getPhone())
                .password(passwordEncoder.encode(user.getPassword())).status(UserStatusMessage.ACTIVE).isActive(true).isMailVerified(false).address(user.getAddress()).build();
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
            if (!userAuthenticated.getStatus().equals(UserStatusMessage.ACTIVE)) {
                throw new UserDisableException(UserErrorMessage.USER_UNAVAILABLE);
            }
            String token = Utils.buildJWT(authenticate, userAuthenticated, secretKey, jwtConfig);
            loginResponseDTO = LoginResponseDto.builder()
                    .userId(userAuthenticated.getId())
                    .firstName(userAuthenticated.getFirstName())
                    .lastName(userAuthenticated.getLastName())
                    .username(userAuthenticated.getUsername())
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
        Optional<User> user = userRepository.findById(userId);
        user.get().setStatus(UserStatusMessage.INACTIVE);
        user.get().setActive(false);
        userRepository.save(user.get());
        UserDto userDto = modelMapper.map(user.get(), UserDto.class);
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
        Optional<User> user = Optional.ofNullable(userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(UserErrorMessage.USER_NOT_FOUND)));
        UserDto userDto = modelMapper.map(user.get(), UserDto.class);
        return userDto;
    }

    @Override
    public UserDto addUser(AddUserDto user) {
        Optional<User> userTmp = userRepository.findUserByEmail(user.getEmail());
        if (userTmp.isPresent()) {
            throw new UserNameExistException(UserErrorMessage.USER_NAME_EXIST);
        }
        Optional<Role> role = Optional.ofNullable(roleRepository.findByName(user.getRoleName()).orElseThrow(() -> new RoleNotFoundException(RoleErrorMessage.ROLE_NOT_FOUND)));
        User userBuild = User.builder()
                .status("ACTIVE")
                .isActive(true)
                .address(user.getAddress())
                .role(role.get())
                .phone(user.getPhone())
                .email(user.getEmail())
                .avatar(user.getAvatar())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .username(user.getUsername())
                .password(passwordEncoder.encode(user.getPassword()))
                .build();
        User inRepo = userRepository.save(userBuild);
        UserDto userDTO = modelMapper.map(inRepo, UserDto.class);
        return userDTO;
    }
}
