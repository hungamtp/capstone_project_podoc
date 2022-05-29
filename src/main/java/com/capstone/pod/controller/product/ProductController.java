package com.capstone.pod.controller.product;

import com.capstone.pod.constant.product.ProductSuccessMessage;
import com.capstone.pod.constant.role.RolePreAuthorize;
import com.capstone.pod.dto.http.ResponseDto;
import com.capstone.pod.dto.product.AddProductDto;
import com.capstone.pod.dto.product.GetAllProductDto;
import com.capstone.pod.dto.product.ProductDto;
import com.capstone.pod.entities.Product;
import com.capstone.pod.services.ProductService;
import com.capstone.pod.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("product")
public class ProductController {
    private final ProductService productService;
    @PostMapping
    @PreAuthorize(RolePreAuthorize.ROLE_ADMIN)
    public ResponseEntity<ResponseDto> addProduct(@Validated @RequestBody AddProductDto addProductDto){
        ResponseDto<ProductDto> responseDto = new ResponseDto();
        ProductDto productDto = productService.addProduct(addProductDto);
        responseDto.setData(productDto);
        responseDto.setSuccessMessage(ProductSuccessMessage.ADD_PRODUCT_SUCCESS);
        return ResponseEntity.ok().body(responseDto);
    }
    @GetMapping
    @PreAuthorize(RolePreAuthorize.ROLE_ADMIN_AND_USER)
    public ResponseEntity<ResponseDto> getAllProduct(@RequestParam Integer pageNumber,
                                                     @RequestParam Integer pageSize,
                                                     @RequestParam String search,
                                                     @RequestParam String sort){
        ResponseDto<Page<ProductDto>> responseDTO = new ResponseDto();

        Pageable pageable = PageRequest.of(Optional.ofNullable(pageNumber)
                .orElse(0), Optional.ofNullable(pageSize).orElse(9), sort.equals("createDate")
                ? Sort.by("createDate").descending() : Sort.by(!sort.equals("") ? sort : "id")
                .ascending());

        Specification spec = Utils.buildProductSpecifications(search);
        responseDTO.setData(productService.getAllProducts(spec, pageable));
        responseDTO.setSuccessMessage(ProductSuccessMessage.GET_ALL_PRODUCT_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }
}
