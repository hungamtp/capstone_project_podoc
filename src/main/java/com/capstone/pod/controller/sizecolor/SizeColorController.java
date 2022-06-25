package com.capstone.pod.controller.sizecolor;

import com.capstone.pod.constant.product.ProductSuccessMessage;
import com.capstone.pod.constant.role.RolePreAuthorize;
import com.capstone.pod.constant.sizecolor.SizeColorErrorMessage;
import com.capstone.pod.constant.sizecolor.SizeColorSuccessMessage;
import com.capstone.pod.dto.color.ColorDto;
import com.capstone.pod.dto.http.ResponseDto;
import com.capstone.pod.dto.size.SizeDto;
import com.capstone.pod.services.SizeColorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("size-color")
public class SizeColorController {
    private final SizeColorService sizeColorService;
    @GetMapping("/colors")
    @PreAuthorize(RolePreAuthorize.ROLE_ADMIN)
    public ResponseEntity<ResponseDto> getAllColors(@RequestParam int pageNumber, @RequestParam int pageSize){
        ResponseDto<Page<ColorDto>> responseDto = new ResponseDto();
        Pageable pageable = PageRequest.of(pageNumber,pageSize);
        Page<ColorDto> colors = sizeColorService.getAllColor(pageable);
        responseDto.setData(colors);
        responseDto.setSuccessMessage(ProductSuccessMessage.GET_COLORS_SUCCESS);
        return ResponseEntity.ok().body(responseDto);
    }
    @GetMapping("/sizes")
    @PreAuthorize(RolePreAuthorize.ROLE_ADMIN)
    public ResponseEntity<ResponseDto> getAllSizes(@RequestParam int pageNumber, @RequestParam int pageSize){
        ResponseDto<Page<SizeDto>> responseDto = new ResponseDto();
        Pageable pageable = PageRequest.of(pageNumber,pageSize);
        Page<SizeDto> sizes = sizeColorService.getAllSize(pageable);
        responseDto.setData(sizes);
        responseDto.setSuccessMessage(ProductSuccessMessage.GET_SIZES_SUCCESS);
        return ResponseEntity.ok().body(responseDto);
    }
    @PostMapping("/color")
    @PreAuthorize(RolePreAuthorize.ROLE_ADMIN)
    public ResponseEntity<ResponseDto> addColor(@RequestBody ColorDto dto){
        ResponseDto<ColorDto> responseDto = new ResponseDto();
        ColorDto colors = sizeColorService.addColor(dto);
        responseDto.setData(colors);
        responseDto.setSuccessMessage(SizeColorSuccessMessage.ADD_COLOR_SUCCESS);
        return ResponseEntity.ok().body(responseDto);
    }
    @PostMapping("/size")
    @PreAuthorize(RolePreAuthorize.ROLE_ADMIN)
    public ResponseEntity<ResponseDto> addSize(@RequestBody SizeDto dto){
        ResponseDto<SizeDto> responseDto = new ResponseDto();
        SizeDto sizes = sizeColorService.addSize(dto);
        responseDto.setData(sizes);
        responseDto.setSuccessMessage(SizeColorSuccessMessage.ADD_SIZE_SUCCESS);
        return ResponseEntity.ok().body(responseDto);
    }
}
