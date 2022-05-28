package com.capstone.pod.services;

import com.capstone.pod.dto.factory.AddFactoryDto;
import com.capstone.pod.dto.factory.AddFactoryResponse;
import com.capstone.pod.dto.user.UpdateAvatarDto;
import com.capstone.pod.dto.user.UpdatePasswordDto;
import com.capstone.pod.dto.user.UserDto;

public interface FactoryService {
    public AddFactoryResponse addFactory(AddFactoryDto factoryDto);
    public UserDto updatePassword(UpdatePasswordDto user, int userId);
    public UserDto updateAvatar(UpdateAvatarDto avatar, int userId);
}
