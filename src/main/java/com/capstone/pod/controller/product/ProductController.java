package com.capstone.pod.controller.product;

import com.capstone.pod.constant.product.ProductSuccessMessage;
import com.capstone.pod.constant.role.RolePreAuthorize;
import com.capstone.pod.dto.http.ResponseDto;
import com.capstone.pod.dto.product.AddProductDto;
import com.capstone.pod.dto.product.ProductDto;
import com.capstone.pod.entities.Product;
import com.capstone.pod.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("product")
public class ProductController {
    private final ProductService productService;
    @PostMapping
    @PreAuthorize(RolePreAuthorize.ROLE_ADMIN)
    public ResponseEntity<ResponseDto> addProduct(@RequestBody AddProductDto addProductDto){
        ResponseDto<ProductDto> responseDto = null;
        ProductDto productDto = productService.addProduct(addProductDto);
        responseDto.setData(productDto);
        responseDto.setSuccessMessage(ProductSuccessMessage.ADD_PRODUCT_SUCCESS);
        return ResponseEntity.ok().body(responseDto);
    }
}
