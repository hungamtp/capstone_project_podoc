package com.capstone.pod.services;

import com.capstone.pod.dto.category.CategoryDto;
import com.capstone.pod.dto.category.UpdateCategoryDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CategoryService {
    Page<CategoryDto> getAllCategory(Pageable pageable);

    public CategoryDto deleteCategory(int categoryId);
    public CategoryDto addCategory(CategoryDto categoryDto);
    public CategoryDto updateCategory(UpdateCategoryDto categoryDto, int categoryId);

}
