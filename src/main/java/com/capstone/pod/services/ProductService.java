package com.capstone.pod.services;

import com.capstone.pod.dto.product.AddProductDto;
import com.capstone.pod.dto.product.GetAllProductDto;
import com.capstone.pod.dto.product.GetProductByIdDto;
import com.capstone.pod.dto.product.ProductDto;
import com.capstone.pod.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;


public interface ProductService {
      public ProductDto addProduct(AddProductDto productDto);
      public Page<GetAllProductDto> getAllProducts(Specification<Product> specification, Pageable pageable);
      public Page<GetAllProductDto> getAllProductsByAdmin(Specification<Product> specification, Pageable pageable);

//    public GetProductByIdDto addProduct(GetProductByIdDto dto);
//
//    public GetProductByIdDto updateProduct(GetProductByIdDto dto, Long productId);
//
//    public GetProductByIdDto deleteProduct(Long productId);
//
    public GetProductByIdDto getProductById(Integer id);
    public GetProductByIdDto getProductByIdAdmin(Integer id);
}
