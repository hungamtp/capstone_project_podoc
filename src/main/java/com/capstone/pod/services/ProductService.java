package com.capstone.pod.services;

import com.capstone.pod.dto.product.GetAllProductDto;
import com.capstone.pod.dto.product.GetProductByIdDto;
import com.capstone.pod.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;


public interface ProductService {
    public Page<GetAllProductDto> getAllProducts(Specification<Product> specification, Pageable pageable);

//    public GetProductByIdDto addProduct(GetProductByIdDto dto);
//
//    public GetProductByIdDto updateProduct(GetProductByIdDto dto, Long productId);
//
//    public GetProductByIdDto deleteProduct(Long productId);
//
    public GetProductByIdDto getProductById(Integer id);
}
