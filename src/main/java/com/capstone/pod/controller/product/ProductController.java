package com.capstone.pod.controller.product;

import com.capstone.pod.constant.product.ProductSuccessMessage;
import com.capstone.pod.constant.role.RolePreAuthorize;
import com.capstone.pod.dto.color.ColorInDesignDto;
import com.capstone.pod.dto.common.PageDTO;
import com.capstone.pod.dto.http.ResponseDto;
import com.capstone.pod.dto.product.*;
import com.capstone.pod.dto.sizecolor.SizeColorByProductIdDto;
import com.capstone.pod.dto.sizecolor.SizeColorDto;
import com.capstone.pod.dto.sizecolor.SizeColorInRawProductDto;
import com.capstone.pod.dto.sizecolor.SizeColorReturnDto;
import com.capstone.pod.dto.utils.Utils;
import com.capstone.pod.entities.Product_;
import com.capstone.pod.services.ProductService;
import com.capstone.pod.services.SizeColorService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.PermitAll;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("product")
public class ProductController {
    private final ProductService productService;
    private final SizeColorService sizeColorService;
    @PostMapping
    @PreAuthorize(RolePreAuthorize.ROLE_ADMIN)
    public ResponseEntity<ResponseDto> addProduct(@Validated @RequestBody AddProductDto addProductDto){
        ResponseDto<ProductDto> responseDto = new ResponseDto();
        ProductDto productDto = productService.addProduct(addProductDto);
        responseDto.setData(productDto);
        responseDto.setSuccessMessage(ProductSuccessMessage.ADD_PRODUCT_SUCCESS);
        return ResponseEntity.ok().body(responseDto);
    }
    @PostMapping("blueprint")
    @PreAuthorize(RolePreAuthorize.ROLE_ADMIN)
    public ResponseEntity<ResponseDto> addProductBluePrint(@RequestParam String productId, @Validated @RequestBody AddProductBluePrintDto addProductBluePrintDto){
        ResponseDto<AddProductBluePrintDto> responseDto = new ResponseDto();
        AddProductBluePrintDto productDto = productService.addProductBluePrint(productId, addProductBluePrintDto);
        responseDto.setData(productDto);
        responseDto.setSuccessMessage(ProductSuccessMessage.ADD_PRODUCT_BLUEPRINT_SUCCESS);
        return ResponseEntity.ok().body(responseDto);
    }
    @PutMapping("blueprint")
    @PreAuthorize(RolePreAuthorize.ROLE_ADMIN)
    public ResponseEntity<ResponseDto> updateProductBluePrint(@RequestParam String blueprintId, @Validated @RequestBody EditProductBluePrintDto addProductBluePrintDto){
        ResponseDto<EditProductBluePrintDto> responseDto = new ResponseDto();
        EditProductBluePrintDto productDto = productService.editProductBluePrint(blueprintId, addProductBluePrintDto);
        responseDto.setData(productDto);
        responseDto.setSuccessMessage(ProductSuccessMessage.ADD_PRODUCT_BLUEPRINT_SUCCESS);
        return ResponseEntity.ok().body(responseDto);
    }
    @PutMapping("{id}")
    @PreAuthorize(RolePreAuthorize.ROLE_ADMIN)
    public ResponseEntity<ResponseDto> updateProduct(@Validated @RequestBody UpdateProductDto updateProductDto, @PathVariable(name="id") String productId){
        ResponseDto<ProductReturnDto> responseDto = new ResponseDto();
        ProductReturnDto productDto = productService.updateProduct(updateProductDto, productId);
        responseDto.setData(productDto);
        responseDto.setSuccessMessage(ProductSuccessMessage.UPDATE_PRODUCT_SUCCESS);
        return ResponseEntity.ok().body(responseDto);
    }
    @GetMapping
    public ResponseEntity<ResponseDto> getAllProduct(@RequestParam Integer pageNumber,
                                                     @RequestParam Integer pageSize,
                                                     @RequestParam String search,
                                                     @RequestParam String sort){
        ResponseDto<PageDTO> responseDTO = new ResponseDto();
        Pageable pageable = PageRequest.of(Optional.ofNullable(pageNumber)
                .orElse(0), Optional.ofNullable(pageSize).orElse(9), sort.equals("createDate")
                ? Sort.by("createDate").descending() : Sort.by(!sort.equals("") ? sort : "id")
                .ascending());
        search = StringUtils.isEmpty(search) ? "isDeleted:false,isPublic:true" : search.concat(",isDeleted:false,isPublic:true");
        Specification spec = Utils.buildProductSpecifications(search);
        responseDTO.setData(productService.getAllProducts(spec, pageable));
        responseDTO.setSuccessMessage(ProductSuccessMessage.GET_ALL_PRODUCT_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }
    @GetMapping("/admin")
    @PreAuthorize(RolePreAuthorize.ROLE_ADMIN)
    public ResponseEntity<ResponseDto> getAllProductAdmin(@RequestParam Integer pageNumber,
                                                     @RequestParam Integer pageSize,
                                                     @RequestParam String search,
                                                     @RequestParam String sort){
        ResponseDto<Page<ProductDto>> responseDTO = new ResponseDto();

        Pageable pageable = PageRequest.of(Optional.ofNullable(pageNumber)
                .orElse(0), Optional.ofNullable(pageSize).orElse(9),
                sort.equals(Product_.CREATE_DATE) ? Sort.by(Product_.CREATE_DATE).descending() : Sort.by(!sort.equals("") ? sort : "id").ascending());
        Specification spec = Utils.buildProductSpecifications(search);
        responseDTO.setData(productService.getAllProductsByAdmin(spec, pageable));
        responseDTO.setSuccessMessage(ProductSuccessMessage.GET_ALL_PRODUCT_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto> getProductById(@PathVariable(name = "id") String productId){
        ResponseDto<ProductDetailDto> responseDTO = new ResponseDto();
        responseDTO.setData(productService.getProductById(productId));
        responseDTO.setSuccessMessage(ProductSuccessMessage.GET_PRODUCT_BY_ID_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("design/{id}")
    public ResponseEntity<ResponseDto> getBluePrintByProductId(@PathVariable(name = "id") String productId){
        ResponseDto<List<ProductBluePrintDto>> responseDTO = new ResponseDto();
        responseDTO.setData(productService.getProductBluePrintByProductId(productId));
        responseDTO.setSuccessMessage(ProductSuccessMessage.GET_BLUEPRINTS_PRODUCT_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }
    @GetMapping("/admin/{id}")
    @PreAuthorize(RolePreAuthorize.ROLE_ADMIN_AND_FACTORY)
    public ResponseEntity<ResponseDto> getProductByIdAdmin(@PathVariable(name = "id") String productId){
        ResponseDto<ProductByAdminDto> responseDTO = new ResponseDto();
        responseDTO.setData(productService.getProductByIdAdmin(productId));
        responseDTO.setSuccessMessage(ProductSuccessMessage.GET_PRODUCT_BY_ID_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }
    @GetMapping("/admin/size-color/{id}")
    @PreAuthorize(RolePreAuthorize.ROLE_ADMIN)
    public ResponseEntity<ResponseDto> getSizeColorProductById(@PathVariable(name = "id") String productId){
        ResponseDto<SizeColorByProductIdDto> responseDTO = new ResponseDto();
        responseDTO.setData(productService.getSizesAndColorByProductId(productId));
        responseDTO.setSuccessMessage(ProductSuccessMessage.GET_SIZES_COLORS_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }
    @GetMapping("/colors")
    @PermitAll
    public ResponseEntity<ResponseDto> getColorsByProductIdAndFactoryId(@RequestParam String productId, @RequestParam String factoryId){
        ResponseDto<List<ColorInDesignDto>> responseDTO = new ResponseDto();
        responseDTO.setData(productService.getColorsByProductNameAndFactoryName(productId, factoryId));
        responseDTO.setSuccessMessage(ProductSuccessMessage.GET_COLORS_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }
    @GetMapping("/product-for-factory")
    @PreAuthorize(RolePreAuthorize.ROLE_ADMIN)
    public ResponseEntity<ResponseDto> getProductFactoryNotHaveYet(@RequestParam String factoryId, @RequestParam @Nullable String productName){
        ResponseDto<List<GetProductFactoryDto>> responseDTO = new ResponseDto();
        responseDTO.setData(productService.getAllProductForFactoryDoNotHaveYet(factoryId, productName));
        responseDTO.setSuccessMessage(ProductSuccessMessage.GET_PRODUCT_FOR_FACTORY_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }
    @PatchMapping("/{id}")
    @PreAuthorize(RolePreAuthorize.ROLE_ADMIN)
    public ResponseEntity<ResponseDto> deleteProductById(@PathVariable(name="id") String productId){
        ResponseDto<ProductReturnDto> responseDTO = new ResponseDto();
        responseDTO.setData(productService.deleteProduct(productId));
        responseDTO.setSuccessMessage(ProductSuccessMessage.DELETE_PRODUCT_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }
    @PatchMapping("/publish/{id}")
    @PreAuthorize(RolePreAuthorize.ROLE_ADMIN)
    public ResponseEntity<ResponseDto> publishProduct(@PathVariable(name="id") String productId){
        ResponseDto<ProductReturnDto> responseDTO = new ResponseDto();
        responseDTO.setData(productService.publishProduct(productId));
        responseDTO.setSuccessMessage(ProductSuccessMessage.PUBLISH_PRODUCT_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }
    @PatchMapping("/un-publish/{id}")
    @PreAuthorize(RolePreAuthorize.ROLE_ADMIN)
    public ResponseEntity<ResponseDto> unPublishProduct(@PathVariable(name="id") String productId){
        ResponseDto<ProductReturnDto> responseDTO = new ResponseDto();
        responseDTO.setData(productService.unPublishProduct(productId));
        responseDTO.setSuccessMessage(ProductSuccessMessage.UN_PUBLISH_PRODUCT_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }
    @PostMapping("/size-color/{id}")
    @PreAuthorize(RolePreAuthorize.ROLE_ADMIN)
    public ResponseEntity<ResponseDto> addSizeColorToProduct(@PathVariable(name = "id") String id, @Validated @RequestBody SizeColorDto dto){
        ResponseDto<List<SizeColorReturnDto>> responseDto = new ResponseDto();
        List<SizeColorReturnDto> sizeColorDtos = sizeColorService.addSizeColor(id, dto);
        responseDto.setData(sizeColorDtos);
        responseDto.setSuccessMessage(ProductSuccessMessage.ADD_SIZE_COLOR_SUCCESS);
        return ResponseEntity.ok().body(responseDto);
    }
    @GetMapping("size-color-raw/{id}")
    @PreAuthorize(RolePreAuthorize.ROLE_ADMIN)
    public ResponseEntity<ResponseDto> getSizeColorByRawProductId(@PathVariable(name = "id") String id){
        ResponseDto<List<SizeColorInRawProductDto>> responseDto = new ResponseDto();
        List<SizeColorInRawProductDto> sizeColorDtos = productService.getColorSizeMapByColorByProductId(id);
        responseDto.setData(sizeColorDtos);
        responseDto.setSuccessMessage(ProductSuccessMessage.GET_SIZES_COLORS_SUCCESS);
        return ResponseEntity.ok().body(responseDto);
    }
}
