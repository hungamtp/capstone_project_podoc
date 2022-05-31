package com.capstone.pod.services.implement;

import com.capstone.pod.constant.category.CategoryErrorMessage;
import com.capstone.pod.constant.product.ProductErrorMessage;
import com.capstone.pod.dto.product.AddProductDto;
import com.capstone.pod.dto.product.GetAllProductDto;
import com.capstone.pod.dto.product.ProductDto;
import com.capstone.pod.dto.product.UpdateProductDto;
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
        Category category = categoryRepository.findByName(productDto.getCategoryName()).orElseThrow(() -> new CategoryNotFoundException(CategoryErrorMessage.CATEGORY_NAME_NOT_FOUND));
        Product product = Product.builder().name(productDto.getName())
                .description(productDto.getDescription()).category(category).isDeleted(false).isPublic(false).build();
        List<ProductImages> imagesList = new ArrayList<>();
        for (int i = 0; i < productDto.getImages().size(); i++) {
            imagesList.add(ProductImages.builder().product(product).image(productDto.getImages().get(i)).build());
        }
        product.setProductImages(imagesList);
        return modelMapper.map(productRepository.save(product),ProductDto.class);
    }
    @Override
    public ProductDto updateProduct(UpdateProductDto productDto,int productId) {
        Product productInRepo = productRepository.findById(productId).orElseThrow(()->new ProductNotFoundException(ProductErrorMessage.PRODUCT_NOT_EXIST));
        Category category = categoryRepository.findByName(productDto.getCategoryName()).orElseThrow(() -> new CategoryNotFoundException(CategoryErrorMessage.CATEGORY_NAME_NOT_FOUND));
        List<ProductImages> imagesList = new ArrayList<>();
        for (int i = 0; i < productDto.getImages().size(); i++) {
            imagesList.add(ProductImages.builder().product(productInRepo).image(productDto.getImages().get(i)).build());
        }
        productInRepo.setProductImages(imagesList);
        productInRepo.setName(productDto.getName());
        productInRepo.setCategory(category);
        productInRepo.setDescription(productDto.getDescription());
        return modelMapper.map(productRepository.save(productInRepo),ProductDto.class);
    }
    @Override
    public ProductDto publishProduct(int productId) {
        Product product = productRepository.findById(productId).orElseThrow(()->new ProductNotFoundException(ProductErrorMessage.PRODUCT_NOT_EXIST));
        product.setPublic(true);
        return modelMapper.map(productRepository.save(product),ProductDto.class);
    }

    @Override
    public Page<GetAllProductDto> getAllProducts(Specification<Product> specification, Pageable pageable) {
        Page<Product> pageProduct = productRepository.findAll(specification, pageable);
        List<Product> listProductFiltered = pageProduct.stream().filter(product -> {return !product.isDeleted() && product.isPublic();}).collect(Collectors.toList());
        Page<Product> convertListToPageProduct = new PageImpl<>(listProductFiltered,pageable,listProductFiltered.size());
        Page<GetAllProductDto> pageProductDTO = convertListToPageProduct.map(product -> modelMapper.map(product, GetAllProductDto.class));
        return pageProductDTO;

    }
    @Override
    public Page<GetAllProductDto> getAllProductsByAdmin(Specification<Product> specification, Pageable pageable) {
        Page<Product> pageProduct = productRepository.findAll(specification, pageable);
        Page<GetAllProductDto> pageProductDTO = pageProduct.map(product -> modelMapper.map(product, GetAllProductDto.class));
        return pageProductDTO;

    }

    @Override
    public ProductDto deleteProduct(int productId) {
        Product product = productRepository.findById(productId).orElseThrow(()->new ProductNotFoundException(ProductErrorMessage.PRODUCT_NOT_EXIST));
        product.setDeleted(true);
        return modelMapper.map(productRepository.save(product),ProductDto.class);
    }

    @Override
    public ProductDto getProductById(Integer productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ProductNotFoundException(ProductErrorMessage.PRODUCT_NOT_EXIST));
        if(!product.isPublic()||product.isDeleted()) return null;
        return modelMapper.map(product,ProductDto.class);
    }
    @Override
    public ProductDto getProductByIdAdmin(Integer productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ProductNotFoundException(ProductErrorMessage.PRODUCT_NOT_EXIST));
        return modelMapper.map(product,ProductDto.class);
    }
}
