package com.capstone.pod.services.implement;

import com.capstone.pod.constant.category.CategoryErrorMessage;
import com.capstone.pod.constant.product.ProductErrorMessage;
import com.capstone.pod.dto.product.AddProductDto;
import com.capstone.pod.dto.product.GetAllProductDto;
import com.capstone.pod.dto.product.GetProductByIdDto;
import com.capstone.pod.dto.product.ProductDto;
import com.capstone.pod.entities.Category;
import com.capstone.pod.entities.Product;
import com.capstone.pod.entities.ProductImages;
import com.capstone.pod.exceptions.CategoryNotFoundException;
import com.capstone.pod.exceptions.ProductNameExistException;
import com.capstone.pod.exceptions.ProductNotFoundException;
import com.capstone.pod.repositories.CategoryRepository;
import com.capstone.pod.repositories.ProductRepository;
import com.capstone.pod.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImplement implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;


    @Override
    public ProductDto addProduct(AddProductDto productDto) {
       Optional<Product> productInRepo = productRepository.findByName(productDto.getName());
        if(productInRepo.isPresent()){
            throw new ProductNameExistException(ProductErrorMessage.PRODUCT_NAME_EXISTED);
        }
        List<ProductImages> imagesList = new ArrayList<>();
        for (int i = 0; i < productDto.getImages().size(); i++) {
            imagesList.add(ProductImages.builder().image(productDto.getImages().get(i)).build());
        }
        Category category = categoryRepository.findByName(productDto.getCategoryName()).orElseThrow(() -> new CategoryNotFoundException(CategoryErrorMessage.CATEGORY_NAME_NOT_FOUND));
        Product product = Product.builder().productImages(imagesList).name(productDto.getName())
                .description(productDto.getDescription()).category(category).isDeleted(false).isPublic(false).build();
        return modelMapper.map(productRepository.save(product),ProductDto.class);
    }

    @Override
    public Page<GetAllProductDto> getAllProducts(Specification<Product> specification, Pageable pageable) {
        Page<Product> pageProduct = productRepository.findAll(specification, pageable);
        List<Product> listProductFiltered = pageProduct.stream().filter(product -> product.isDeleted() == false).collect(Collectors.toList());
        Page<Product> convertListToPageProduct = new PageImpl<>(listProductFiltered,pageable,listProductFiltered.size());
        Page<GetAllProductDto> pageProductDTO = convertListToPageProduct.map(product -> modelMapper.map(product, GetAllProductDto.class));
        return pageProductDTO;
    }
    @Override
    public GetProductByIdDto getProductById(Integer productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ProductNotFoundException(ProductErrorMessage.PRODUCT_NOT_EXIST));
        return modelMapper.map(product,GetProductByIdDto.class);
    }

}
