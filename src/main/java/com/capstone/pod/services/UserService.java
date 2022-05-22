package com.capstone.pod.services;

import com.capstone.pod.dto.auth.LoginDto;
import com.capstone.pod.dto.auth.LoginResponseDto;
import com.capstone.pod.dto.auth.RegisterResponseDto;
import com.capstone.pod.dto.user.*;
import org.springframework.data.domain.Page;

import javax.management.relation.RoleNotFoundException;


public interface UserService {
    public UserDto findByEmail(String email);
    public RegisterResponseDto register(RegisterUserDto user) throws RoleNotFoundException;
    public LoginResponseDto login(LoginDto user);
    public UserDto deleteUserById(int userId);
    public Page<UserDto> getAllUser(int pageNum, int pageSize);
    public UserDto getUserById(int userId);
    public UserDto addUser(AddUserDto user);
    public UserDto updateUser(UpdateUserDto user,int userId);
    public UserDto updateUserByAdmin(UpdateUserDtoByAdmin user, int userId);
}
