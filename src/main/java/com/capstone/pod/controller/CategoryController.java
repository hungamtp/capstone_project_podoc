package com.capstone.pod.controller;

import com.capstone.pod.constant.category.CategorySuccessMessage;
import com.capstone.pod.constant.role.RolePreAuthorize;
import com.capstone.pod.dto.category.CategoryDto;
import com.capstone.pod.dto.category.CategoryHomePageDto;
import com.capstone.pod.dto.category.UpdateCategoryDto;
import com.capstone.pod.dto.http.ResponseDto;
import com.capstone.pod.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("category")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;
    @GetMapping
    public ResponseEntity<ResponseDto> getAllCategory(@RequestParam int pageNumber, @RequestParam int pageSize){
        ResponseDto<Page<CategoryDto>> responseDto = new ResponseDto();
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<CategoryDto> categoryDtoPage= categoryService.getAllCategory(pageable);
        responseDto.setData(categoryDtoPage);
        responseDto.setSuccessMessage(CategorySuccessMessage.GET_ALL_CATEGORY_SUCCESS);
        return ResponseEntity.ok().body(responseDto);
    }
    @PostMapping
    public ResponseEntity<ResponseDto> addCategory(@RequestBody CategoryDto categoryDto){
        ResponseDto<CategoryDto> responseDto = new ResponseDto();
        CategoryDto category= categoryService.addCategory(categoryDto);
        responseDto.setData(category);
        responseDto.setSuccessMessage(CategorySuccessMessage.ADD_CATEGORY_SUCCESS);
        return ResponseEntity.ok().body(responseDto);
    }
    @PutMapping("/{categoryId}")
    public ResponseEntity<ResponseDto> updateCategory(@RequestBody UpdateCategoryDto categoryDto, @PathVariable(name = "categoryId") String categoryId){
        ResponseDto<CategoryDto> responseDto = new ResponseDto();
        CategoryDto categoryDtoPage= categoryService.updateCategory(categoryDto,categoryId);
        responseDto.setData(categoryDtoPage);
        responseDto.setSuccessMessage(CategorySuccessMessage.UPDATE_CATEGORY_SUCCESS);
        return ResponseEntity.ok().body(responseDto);
    }
    @PatchMapping("/{categoryId}")
    public ResponseEntity<ResponseDto> deleteCategory(@PathVariable(name = "categoryId") String categoryId){
        ResponseDto<CategoryDto> responseDto = new ResponseDto();
        CategoryDto categoryDtoPage= categoryService.deleteCategory(categoryId);
        responseDto.setData(categoryDtoPage);
        responseDto.setSuccessMessage(CategorySuccessMessage.DELETE_CATEGORY_SUCCESS);
        return ResponseEntity.ok().body(responseDto);
    }

    @GetMapping("/all")
    public ResponseEntity<ResponseDto> getAllCategoryProductDetail(){
        ResponseDto<List<CategoryHomePageDto>> responseDto = new ResponseDto();
        responseDto.setData(categoryService.getAllCategory());
        responseDto.setSuccessMessage(CategorySuccessMessage.GET_ALL_CATEGORY_SUCCESS);
        return ResponseEntity.ok().body(responseDto);
    }
}
