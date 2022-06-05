package com.capstone.pod.services.implement;

import com.capstone.pod.dto.category.CategoryDto;
import com.capstone.pod.entities.Category;
import com.capstone.pod.repositories.CategoryRepository;
import com.capstone.pod.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
@RequiredArgsConstructor
@Service
public class CategoryServiceImplement implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    @Override
    public Page<CategoryDto> getAllCategory(Pageable pageable) {
        Page<Category> categoryPage= categoryRepository.findAll(pageable);
        Page<CategoryDto> categoryDtoPage = categoryPage.map(cate->modelMapper.map(cate,CategoryDto.class));
        return categoryDtoPage;
    }
}
