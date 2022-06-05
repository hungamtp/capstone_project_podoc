package com.capstone.pod.controller.category;

import com.capstone.pod.constant.category.CategorySuccessMessage;
import com.capstone.pod.constant.role.RolePreAuthorize;
import com.capstone.pod.dto.category.CategoryDto;
import com.capstone.pod.dto.http.ResponseDto;
import com.capstone.pod.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("category")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;
    @GetMapping
    @PreAuthorize(RolePreAuthorize.ROLE_ADMIN)
    public ResponseEntity<ResponseDto> getAllCategory(@RequestParam int pageNumber, @RequestParam int pageSize){
        ResponseDto responseDto = new ResponseDto();
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<CategoryDto> categoryDtoPage= categoryService.getAllCategory(pageable);
        responseDto.setData(categoryDtoPage);
        responseDto.setSuccessMessage(CategorySuccessMessage.GET_ALL_CATEGORY_SUCCESS);
        return ResponseEntity.ok().body(responseDto);
    }
}
