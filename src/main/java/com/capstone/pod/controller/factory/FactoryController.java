package com.capstone.pod.controller.factory;

import com.capstone.pod.constant.factory.FactorySuccessMessage;
import com.capstone.pod.constant.role.RolePreAuthorize;
import com.capstone.pod.constant.user.UserSuccessMessage;
import com.capstone.pod.dto.factory.*;
import com.capstone.pod.dto.http.ResponseDto;
import com.capstone.pod.dto.order.OrderDetailFactoryDto;
import com.capstone.pod.dto.order.OrderDetailForPrintingDto;
import com.capstone.pod.dto.sizecolor.SizeColorInFactoryDetailDto;
import com.capstone.pod.dto.user.UpdateAvatarDto;
import com.capstone.pod.dto.user.UpdatePasswordDto;
import com.capstone.pod.dto.user.UserDto;
import com.capstone.pod.services.FactoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("factory")
@RequiredArgsConstructor
public class FactoryController {
    private final FactoryService factoryService;

    @PreAuthorize(RolePreAuthorize.ROLE_ADMIN)
    @GetMapping
    public ResponseEntity<ResponseDto> getAll(@RequestParam int pageNumber, @RequestParam int pageSize)
    {
        Pageable pageable = PageRequest.of(0, 10000);
        ResponseDto<Page<FactoryPageResponseDto>> responseDTO = new ResponseDto();
        Page<FactoryPageResponseDto> getAllFactory = factoryService.getAllFactories(pageable);
        responseDTO.setData(getAllFactory);
        responseDTO.setSuccessMessage(UserSuccessMessage.GET_ALL_FACTORY_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }
    @PreAuthorize(RolePreAuthorize.ROLE_ADMIN_AND_FACTORY)
    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto> getFactoryByCredentialId(@PathVariable(name = "id") String id) {
        ResponseDto<FactoryByIdDto> responseDTO = new ResponseDto();
        FactoryByIdDto factory = factoryService.getFactorybyCredentialId(id);
        responseDTO.setData(factory);
        responseDTO.setSuccessMessage(UserSuccessMessage.GET_FACTORY_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }
    @PreAuthorize(RolePreAuthorize.ROLE_ADMIN)
    @PostMapping
    public ResponseEntity<ResponseDto> add(@Validated @RequestBody AddFactoryDto factoryDto)
    {
        ResponseDto<AddFactoryResponse> responseDTO = new ResponseDto();
        AddFactoryResponse addFactory = factoryService.addFactory(factoryDto);
        responseDTO.setData(addFactory);
        responseDTO.setSuccessMessage(UserSuccessMessage.ADD_FATORY_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }
    @PreAuthorize(RolePreAuthorize.ROLE_ADMIN_AND_USER)
    @PostMapping("avatar/{id}")
    public ResponseEntity<ResponseDto> updateAvatar(@Validated @RequestBody UpdateAvatarDto avatar, @PathVariable(name = "id") String id) {
        ResponseDto<UserDto> responseDTO = new ResponseDto();
        UserDto checkDTO = factoryService.updateAvatar(avatar, id);
        responseDTO.setSuccessMessage(FactorySuccessMessage.UPDATE_FACTORY_SUCCESS);
        responseDTO.setData(checkDTO);
        return ResponseEntity.ok().body(responseDTO);
    }
    @PreAuthorize(RolePreAuthorize.ROLE_ADMIN)
    @PatchMapping("collaborating/{id}")
    public ResponseEntity<ResponseDto> updateCollaborating(@RequestParam boolean collaborating, @PathVariable(name = "id") String id) {
        ResponseDto<FactoryPageResponseDto> responseDTO = new ResponseDto();
        factoryService.updateCollaborating(id, collaborating);
        responseDTO.setSuccessMessage(FactorySuccessMessage.UPDATE_COLLABORATING_FACTORY_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }
    @PreAuthorize(RolePreAuthorize.ROLE_USER)
    @PostMapping("password-change/{id}")
    public ResponseEntity<ResponseDto> updatePassword(@Validated @RequestBody UpdatePasswordDto user, @PathVariable(name = "id") String id) {
        ResponseDto<UserDto> responseDTO = new ResponseDto();
        UserDto checkDTO = factoryService.updatePassword(user, id);
        responseDTO.setSuccessMessage(FactorySuccessMessage.UPDATE_FACTORY_SUCCESS);
        responseDTO.setData(checkDTO);
        return ResponseEntity.ok().body(responseDTO);
    }
    @PreAuthorize(RolePreAuthorize.ROLE_ADMIN)
    @PostMapping("add-price")
    public ResponseEntity<ResponseDto> addPriceByFactoryToProduct(@RequestParam String factoryId,@RequestParam String productId,@Validated @RequestBody AddPriceByFactoryDto addPriceByFactoryToProduct ) {
        ResponseDto<Void> responseDTO = new ResponseDto();
        factoryService.addPriceByFactoryToProduct(factoryId, productId, addPriceByFactoryToProduct.getPrice(),addPriceByFactoryToProduct.getMaterial());
        responseDTO.setSuccessMessage(FactorySuccessMessage.ADD_PRICE_TO_PRODUCT_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }
    @PreAuthorize(RolePreAuthorize.ROLE_ADMIN)
    @PatchMapping("update-price")
    public ResponseEntity<ResponseDto> apdatePriceByFactoryToProduct(@RequestParam String factoryId, @RequestParam String productId,@Validated @RequestBody AddPriceByFactoryDto addPriceByFactoryToProduct) {
        ResponseDto<Void> responseDTO = new ResponseDto();
        factoryService.updatePriceByFactoryToProduct(factoryId, productId, addPriceByFactoryToProduct.getPrice(), addPriceByFactoryToProduct.getMaterial());
        responseDTO.setSuccessMessage(FactorySuccessMessage.UPDATE_PRICE_TO_PRODUCT_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }
    @PreAuthorize(RolePreAuthorize.ROLE_ADMIN)
    @PostMapping("add-size-color")
    public ResponseEntity<ResponseDto> addSizeAndColorByFactoryToProduct(@RequestParam String factoryId, @RequestParam String productId, @RequestBody List<SizeColorInFactoryDetailDto> sizeColors) {
        ResponseDto<Void> responseDTO = new ResponseDto();
        factoryService.addSizeColorToProduct(factoryId, productId, sizeColors);
        responseDTO.setSuccessMessage(FactorySuccessMessage.ADD_SIZE_COLOR_TO_PRODUCT_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }
    @PreAuthorize(RolePreAuthorize.ROLE_FACTORY)
    @GetMapping("order-details/{id}")
    public ResponseEntity<ResponseDto> getAllOrderDetailsForFactoryByCredentialId(@PathVariable(name = "id") String credentialId, @RequestParam int pageNumber, @RequestParam int pageSize) {
        ResponseDto<Page<OrderDetailFactoryDto>> responseDTO = new ResponseDto();
        Pageable pageable = PageRequest.of(pageNumber,pageSize);
        Page<OrderDetailFactoryDto> page  = factoryService.getAllOrderDetailsForFactoryByCredentialId(pageable, credentialId);
        responseDTO.setData(page);
        responseDTO.setSuccessMessage(FactorySuccessMessage.GET_ALL_ORDER_DETAIL_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }
    @PreAuthorize(RolePreAuthorize.ROLE_FACTORY)
    @GetMapping("order-details-printing")
    public ResponseEntity<ResponseDto> getOrderDetailsForFactoryPrinting(@RequestParam String orderId, @RequestParam String designId, @RequestParam String credentialId) {
        ResponseDto<OrderDetailForPrintingDto> responseDTO = new ResponseDto();
        OrderDetailForPrintingDto orderDetailForPrintingDto  = factoryService.getAllOrderDetailsForPrintingByOrderDetailsId(orderId, designId, credentialId);
        responseDTO.setData(orderDetailForPrintingDto);
        responseDTO.setSuccessMessage(FactorySuccessMessage.GET_ORDER_DETAIL_FOR_PRINTING_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

}