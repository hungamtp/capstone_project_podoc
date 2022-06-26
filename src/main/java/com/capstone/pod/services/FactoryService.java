package com.capstone.pod.services;

import com.capstone.pod.dto.factory.AddFactoryDto;
import com.capstone.pod.dto.factory.AddFactoryResponse;
import com.capstone.pod.dto.factory.FactoryByIdDto;
import com.capstone.pod.dto.factory.FactoryPageResponseDto;
import com.capstone.pod.dto.sizecolor.SizeColorDto;
import com.capstone.pod.dto.sizecolor.SizeColorInFactoryDetailDto;
import com.capstone.pod.dto.user.UpdateAvatarDto;
import com.capstone.pod.dto.user.UpdatePasswordDto;
import com.capstone.pod.dto.user.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FactoryService {
    public Page<FactoryPageResponseDto> getAllFactories(Pageable pageable);
    public AddFactoryResponse addFactory(AddFactoryDto factoryDto);
    public FactoryByIdDto getFactorybyCredentialId(int credentialId);
    public UserDto updatePassword(UpdatePasswordDto user, int userId);
    public UserDto updateAvatar(UpdateAvatarDto avatar, int userId);
    public void updateCollaborating(int factoryId, boolean isCollaborating);
    public void addSizeColorToProduct(int factoryId, int productId, List<SizeColorInFactoryDetailDto> sizeColors);
    public void addPriceByFactoryToProduct(int factoryId, int productId, double price);
    public void updatePriceByFactoryToProduct(int factoryId, int productId, double price);
}
