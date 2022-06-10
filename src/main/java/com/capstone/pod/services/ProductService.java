package com.capstone.pod.services;

import com.capstone.pod.dto.common.PageDTO;
import com.capstone.pod.dto.product.AddProductDto;
import com.capstone.pod.dto.product.ProductDetailDto;
import com.capstone.pod.dto.product.ProductDto;
import com.capstone.pod.dto.product.UpdateProductDto;
import com.capstone.pod.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;


public interface ProductService {
      ProductDto addProduct(AddProductDto productDto);
      PageDTO getAllProducts(Specification<Product> specification, Pageable pageable);
      Page<ProductDto> getAllProductsByAdmin(Specification<Product> specification, Pageable pageable);

      ProductDto updateProduct(UpdateProductDto dto, int productId);
      ProductDto publishProduct(int productId);
      ProductDto deleteProduct(int productId);

      ProductDetailDto getProductById(Integer id);
      ProductDto getProductByIdAdmin(Integer id);
}
