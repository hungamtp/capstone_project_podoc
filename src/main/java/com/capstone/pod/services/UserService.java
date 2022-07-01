package com.capstone.pod.services;

import com.capstone.pod.dto.user.*;
import org.springframework.data.domain.Page;


public interface UserService {

    public UserDto deleteUserById(String userId);
    public Page<UserDto> getAllUser(int pageNum, int pageSize);
    public UserDto getUserById(String userId);
    public UserDto getUserByIdRoleAdmin(String userId);
    public UserDto addUser(AddUserDto user);
    public UserDto addAdmin(AddUserDto user);
    public UserDto addFactory(AddUserDto user);
    public UserDto updateUser(UpdateUserDto user,String userId);
    public UserDto updateUserByAdmin(UpdateUserDtoByAdmin user, String userId);
    public UserDto updatePassword(UpdatePasswordDto user,String userId);
    public UserDto updateAvatar(UpdateAvatarDto avatar,String userId);
    public UserDto findByEmail(String email);
    public Page<UserDto> getAllByRoleName(int pageNumber, int pageSize, String roleName);
}
