package com.capstone.pod.controller.designedproduct;

import com.capstone.pod.constant.designedproduct.DesignedProductSuccessMessage;
import com.capstone.pod.constant.role.RolePreAuthorize;
import com.capstone.pod.dto.designedProduct.*;
import com.capstone.pod.dto.http.ResponseDto;
import com.capstone.pod.services.DesignedProductService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("design")
@RequiredArgsConstructor
public class DesignedProductController {
    private final DesignedProductService designedProductService;

    @GetMapping("/4highestDRateDesignedProduct")
    public ResponseEntity<ResponseDto> get4highestDRateDesignedProduct() {
        ResponseDto<List<DesignedProductDTO>> responseDto = new ResponseDto();
        responseDto.setData(designedProductService.get4HighestRateDesignedProduct());
        return ResponseEntity.ok().body(responseDto);
    }

    @GetMapping("/4highestDRateDesignedProduct/{productId}")
    public ResponseEntity<ResponseDto> get4highestDRateDesignedProductByProductId(@PathVariable int productId) {
        ResponseDto<List<DesignedProductDTO>> responseDto = new ResponseDto();
        responseDto.setData(designedProductService.get4HighestRateDesignedProductByProductId(productId));
        return ResponseEntity.ok().body(responseDto);
    }
    @PreAuthorize(RolePreAuthorize.ROLE_USER)
    @PostMapping
    public ResponseEntity<ResponseDto> addDesignedProduct(@Validated @RequestBody DesignedProductSaveDto dto,@RequestParam int productId ,@RequestParam int factoryId) {
        ResponseDto<DesignedProductReturnDto> responseDto = new ResponseDto();
        responseDto.setData(designedProductService.addDesignedProduct(dto, productId, factoryId));
        responseDto.setSuccessMessage(DesignedProductSuccessMessage.ADD_DESIGNED_PRODUCT_SUCCESS);
        return ResponseEntity.ok().body(responseDto);
    }
    @PreAuthorize(RolePreAuthorize.ROLE_USER)
    @PostMapping("/edit/{productId}")
    public ResponseEntity<ResponseDto> editDesignedProduct(@Validated @RequestBody DesignedProductSaveDto dto,@PathVariable int productId) {
        ResponseDto<DesignedProductReturnDto> responseDto = new ResponseDto();
        responseDto.setData(designedProductService.editDesignedProduct(dto, productId));
        responseDto.setSuccessMessage(DesignedProductSuccessMessage.EDIT_DESIGNED_PRODUCT_SUCCESS);
        return ResponseEntity.ok().body(responseDto);
    }
    @PreAuthorize(RolePreAuthorize.ROLE_USER)
    @DeleteMapping("/{designId}")
    public ResponseEntity<ResponseDto> deleteDesignedProduct(@PathVariable int designId) {
        ResponseDto<DesignedProductReturnDto> responseDto = new ResponseDto();
        designedProductService.deleteDesignedProduct(designId);
        responseDto.setSuccessMessage(DesignedProductSuccessMessage.DELETE_DESIGNED_PRODUCT_SUCCESS);
        return ResponseEntity.ok().body(responseDto);
    }    @PreAuthorize(RolePreAuthorize.ROLE_USER)
    @PatchMapping("/publish/{designId}")
    public ResponseEntity<ResponseDto> publishDesignedProduct(@PathVariable int designId) {
        ResponseDto<DesignedProductReturnDto> responseDto = new ResponseDto();
        responseDto.setData(designedProductService.publishDesignedProduct(designId));
        responseDto.setSuccessMessage(DesignedProductSuccessMessage.PUBLISH_DESIGNED_PRODUCT_SUCCESS);
        return ResponseEntity.ok().body(responseDto);
    }
    @PreAuthorize(RolePreAuthorize.ROLE_USER)
    @PatchMapping("/un-publish/{designId}")
    public ResponseEntity<ResponseDto> unPublishDesignedProduct(@PathVariable int designId) {
        ResponseDto<DesignedProductReturnDto> responseDto = new ResponseDto();
        responseDto.setData(designedProductService.unPublishDesignedProduct(designId));
        responseDto.setSuccessMessage(DesignedProductSuccessMessage.UN_PUBLISH_DESIGNED_PRODUCT_SUCCESS);
        return ResponseEntity.ok().body(responseDto);
    }
    @PreAuthorize(RolePreAuthorize.ROLE_USER)
    @GetMapping("/{designId}")
    public ResponseEntity<ResponseDto> getDesignedProductById(@PathVariable int designId) {
        ResponseDto<DesignedProductReturnDto> responseDto = new ResponseDto();
        responseDto.setData(designedProductService.getDesignedProductById(designId));
        responseDto.setSuccessMessage(DesignedProductSuccessMessage.GET_DESIGNED_PRODUCT_BY_ID_SUCCESS);
        return ResponseEntity.ok().body(responseDto);
    }
    @PreAuthorize(RolePreAuthorize.ROLE_USER)
    @GetMapping("/view/{userId}")
    public ResponseEntity<ResponseDto> viewOtherDesigned(@RequestParam Integer pageNumber, @RequestParam Integer pageSize,  @PathVariable(name = "userId") Integer userId) {
        ResponseDto<Page<ViewOtherDesignDto>> responseDto = new ResponseDto();
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        responseDto.setData(designedProductService.viewDesignOfOthersByUserId(pageable, userId));
        responseDto.setSuccessMessage(DesignedProductSuccessMessage.VIEW_OTHERS_DESIGNED_PRODUCT_SUCCESS);
        return ResponseEntity.ok().body(responseDto);
    }
    @PreAuthorize(RolePreAuthorize.ROLE_USER)
    @PatchMapping("/price/{designId}")
    public ResponseEntity<ResponseDto> updateDesignPrice(@Validated @RequestBody DesignedProductPriceDto dto, @PathVariable int designId) {
        ResponseDto<DesignedProductReturnDto> responseDto = new ResponseDto();
        responseDto.setData(designedProductService.editDesignedProductPrice(dto, designId));
        responseDto.setSuccessMessage(DesignedProductSuccessMessage.UPDATE_DESIGNED_PRODUCT_PRICE_SUCCESS);
        return ResponseEntity.ok().body(responseDto);
    }
}
