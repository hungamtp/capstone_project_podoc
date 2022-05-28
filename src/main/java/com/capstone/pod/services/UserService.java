package com.capstone.pod.services;

import com.capstone.pod.dto.factory.AddFactoryDto;
import com.capstone.pod.dto.factory.AddFactoryResponse;
import com.capstone.pod.dto.user.*;
import org.springframework.data.domain.Page;


public interface UserService {

    public UserDto deleteUserById(int userId);
    public Page<UserDto> getAllUser(int pageNum, int pageSize);
    public UserDto getUserById(int userId);
    public UserDto getUserByIdRoleAdmin(int userId);
    public UserDto addUser(AddUserDto user);
    public UserDto addAdmin(AddUserDto user);
    public UserDto updateUser(UpdateUserDto user,int userId);
    public UserDto updateUserByAdmin(UpdateUserDtoByAdmin user, int userId);
    public UserDto updatePassword(UpdatePasswordDto user,int userId);
    public UserDto updateAvatar(UpdateAvatarDto avatar,int userId);

}
