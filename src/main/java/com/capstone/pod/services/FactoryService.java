package com.capstone.pod.services;

import com.capstone.pod.dto.factory.AddFactoryDto;
import com.capstone.pod.dto.factory.AddFactoryResponse;
import com.capstone.pod.dto.factory.FactoryPageResponseDto;
import com.capstone.pod.dto.user.UpdateAvatarDto;
import com.capstone.pod.dto.user.UpdatePasswordDto;
import com.capstone.pod.dto.user.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FactoryService {
    public Page<FactoryPageResponseDto> getAllFactories(Pageable pageable);
    public AddFactoryResponse addFactory(AddFactoryDto factoryDto);
    public AddFactoryResponse getFactoryById(int factoryId);
    public UserDto updatePassword(UpdatePasswordDto user, int userId);
    public UserDto updateAvatar(UpdateAvatarDto avatar, int userId);
    public void updateCollaborating(int factoryId, boolean isCollaborating);
}
