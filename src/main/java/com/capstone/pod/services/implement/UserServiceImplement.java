package com.capstone.pod.services.implement;

import com.capstone.pod.dto.auth.LoginDto;
import com.capstone.pod.dto.auth.LoginResponseDto;
import com.capstone.pod.dto.user.UserDto;
import com.capstone.pod.entities.User;
import com.capstone.pod.exceptions.UserDisableException;
import com.capstone.pod.jwt.JwtConfig;
import com.capstone.pod.repositories.UserRepository;
import com.capstone.pod.services.UserService;
import com.capstone.pod.utils.Utils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
@Service
public class UserServiceImplement implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SecretKey secretKey;
    @Autowired
    private JwtConfig jwtConfig;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;


    @Autowired
    private ModelMapper modelMapper;
    @Override
    public UserDto findByUserName(String username) {
        User user = userRepository.findUserByUsername(username);
        if (user == null) throw new UsernameNotFoundException("USER NAME NOT FOUND!");
        return modelMapper.map(user, UserDto.class);
    }

    public UserDto findByEmail(String email) {
        User user = userRepository.findUserByEmail(email);
        if (user == null) throw new UsernameNotFoundException("EMAIL NOT FOUND!");
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public LoginResponseDto login(LoginDto user) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword());
        LoginResponseDto loginResponseDTO = null;
        Authentication authenticate = authenticationManager.authenticate(authentication);
        if (authenticate.isAuthenticated()) {
            User userAuthenticated = userRepository.findUserByUsername(user.getEmail());
            if (!userAuthenticated.getStatus().equals("active")) {
                throw new UserDisableException("THIS USER IS NOT AVAILABLE AT THIS TIME");
            }
            String token = Utils.buildJWT(authenticate, userAuthenticated, secretKey, jwtConfig);
            loginResponseDTO = LoginResponseDto.builder()
                    .userId(userAuthenticated.getId())
                    .firstName(userAuthenticated.getFirstName())
                    .lastName(userAuthenticated.getLastName())
                    .username(userAuthenticated.getUsername())
                    .email(userAuthenticated.getEmail())
                    .avatar(userAuthenticated.getAvatar())
                    .roleName(userAuthenticated.getRole().getName())
                    .token(jwtConfig.getTokenPrefix() + token).build();
        }
        return loginResponseDTO;
    }

    @Override
    public UserDto deleteUserById(int userId) {
        return null;
    }

    @Override
    public Page<UserDto> getAllUser(int pageNum, int pageSize) {
        return null;
    }

    @Override
    public UserDto getUserById(int userId) {
        return null;
    }
}
