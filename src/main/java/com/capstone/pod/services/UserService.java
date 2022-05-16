package com.capstone.pod.services;

import com.capstone.pod.dto.auth.LoginDto;
import com.capstone.pod.dto.auth.LoginResponseDto;
import com.capstone.pod.dto.user.UserDto;
import org.springframework.data.domain.Page;


public interface UserService {
    public UserDto findByUserName(String username);
    public UserDto findByEmail(String email);

    public LoginResponseDto login(LoginDto user);


    public UserDto deleteUserById(int userId);

    public Page<UserDto> getAllUser(int pageNum, int pageSize);

    public UserDto getUserById(int userId);
}
