package com.capstone.pod.controller.sizecolor;

import com.capstone.pod.constant.product.ProductSuccessMessage;
import com.capstone.pod.constant.role.RolePreAuthorize;
import com.capstone.pod.dto.color.ColorDto;
import com.capstone.pod.dto.http.ResponseDto;
import com.capstone.pod.dto.product.AddProductDto;
import com.capstone.pod.dto.product.ProductDto;
import com.capstone.pod.dto.size.SizeDto;
import com.capstone.pod.dto.sizecolor.SizeColorDto;
import com.capstone.pod.services.ProductService;
import com.capstone.pod.services.SizeColorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("size-color")
public class SizeColorController {
    private final SizeColorService sizeColorService;
    @GetMapping("/size")
    @PreAuthorize(RolePreAuthorize.ROLE_ADMIN)
    public ResponseEntity<ResponseDto> getAllSize(){
        ResponseDto<List<SizeDto>> responseDto = new ResponseDto();
        List<SizeDto> sizes = sizeColorService.getSizes();
        responseDto.setData(sizes);
        responseDto.setSuccessMessage(ProductSuccessMessage.GET_SIZES_SUCCESS);
        return ResponseEntity.ok().body(responseDto);
    }
    @GetMapping("/color")
    @PreAuthorize(RolePreAuthorize.ROLE_ADMIN)
    public ResponseEntity<ResponseDto> addSizeColortoProduct(){
        ResponseDto<List<ColorDto>> responseDto = new ResponseDto();
        List<ColorDto> colors = sizeColorService.getColors();
        responseDto.setData(colors);
        responseDto.setSuccessMessage(ProductSuccessMessage.GET_COLORS_SUCCESS);
        return ResponseEntity.ok().body(responseDto);
    }
}
