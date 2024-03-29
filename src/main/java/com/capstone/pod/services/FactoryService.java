package com.capstone.pod.services;

import com.capstone.pod.dto.factory.*;
import com.capstone.pod.dto.order.OrderDetailFactoryDto;
import com.capstone.pod.dto.order.OrderDetailForPrintingDto;
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
    public AddFactoryResponse updateFactory(String credentialId, UpdateFactoryDto factoryDto);
    public FactoryByIdDto getFactorybyCredentialId(String credentialId, String productName);
    public UserDto updatePassword(UpdatePasswordDto user, String userId);
    public UserDto updateAvatar(UpdateAvatarDto avatar, String userId);
    public void updateCollaborating(String factoryId, boolean isCollaborating);
    public void addSizeColorToProduct(String factoryId, String productId, List<SizeColorInFactoryDetailDto> sizeColors);
    public void addPriceByFactoryToProduct(String factoryId, String productId, double price,String material);
    public void updatePriceByFactoryToProduct(String factoryId, String productId, double price, String material);

    public List<OrderDetailFactoryDto> getAllOrderDetailsForFactoryByCredentialId(String credentialId);
    public OrderDetailForPrintingDto getAllOrderDetailsForPrintingByOrderDetailsId(String orderId,String designId, String credentialId);
}
