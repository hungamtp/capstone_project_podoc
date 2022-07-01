package com.capstone.pod.services.implement;

import com.capstone.pod.constant.category.CategoryErrorMessage;
import com.capstone.pod.dto.category.CategoryDto;
import com.capstone.pod.dto.category.CategoryHomePageDto;
import com.capstone.pod.dto.category.UpdateCategoryDto;
import com.capstone.pod.entities.Category;
import com.capstone.pod.exceptions.CategoryExistedException;
import com.capstone.pod.exceptions.CategoryNotFoundException;
import com.capstone.pod.repositories.CategoryRepository;
import com.capstone.pod.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @Override
    public CategoryDto deleteCategory(String categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new CategoryNotFoundException(CategoryErrorMessage.CATEGORY_ID_NOT_FOUND));
        category.setDeleted(true);
        return modelMapper.map(categoryRepository.save(category),CategoryDto.class);
    }

    @Override
    public CategoryDto addCategory(CategoryDto categoryDto) {
        Optional<Category> categoryInRepo = categoryRepository.findByName(categoryDto.getName());
        if(categoryInRepo.isPresent()){
            throw new CategoryExistedException(CategoryErrorMessage.CATEGORY_EXISTED_EXCEPTION);
        }
        Category category = Category.builder().name(categoryDto.getName()).image(categoryDto.getImage()).isDeleted(false).build();
        return modelMapper.map(categoryRepository.save(category),CategoryDto.class);
    }

    @Override
    public CategoryDto updateCategory(UpdateCategoryDto categoryDto,String categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new CategoryNotFoundException(CategoryErrorMessage.CATEGORY_ID_NOT_FOUND));
        category.setImage(categoryDto.getImage());
        category.setName(categoryDto.getName());
        return modelMapper.map(categoryRepository.save(category),CategoryDto.class);
    }

    @Override
    public List<CategoryHomePageDto> getAllCategory() {
        List<Category> categories= categoryRepository.findAllByIsDeletedFalse();
        List<CategoryHomePageDto> categoryDtoPage = categories.stream().map(
            cate->modelMapper.map(cate,CategoryHomePageDto.class)).collect(Collectors.toList());
        return categoryDtoPage;
    }
}
