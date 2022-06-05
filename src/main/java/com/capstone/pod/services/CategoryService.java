package com.capstone.pod.services;

import com.capstone.pod.dto.category.CategoryDto;
import com.capstone.pod.entities.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CategoryService {
    Page<CategoryDto> getAllCategory(Pageable pageable);
}
