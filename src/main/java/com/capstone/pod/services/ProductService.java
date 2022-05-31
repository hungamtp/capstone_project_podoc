package com.capstone.pod.services;

import com.capstone.pod.dto.product.AddProductDto;
import com.capstone.pod.dto.product.GetAllProductDto;
import com.capstone.pod.dto.product.ProductDto;
import com.capstone.pod.dto.product.UpdateProductDto;
import com.capstone.pod.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;


public interface ProductService {
      public ProductDto addProduct(AddProductDto productDto);
      public Page<GetAllProductDto> getAllProducts(Specification<Product> specification, Pageable pageable);
      public Page<GetAllProductDto> getAllProductsByAdmin(Specification<Product> specification, Pageable pageable);

    public ProductDto updateProduct(UpdateProductDto dto, int productId);
    public ProductDto publishProduct(int productId);
    public ProductDto deleteProduct(int productId);

    public ProductDto getProductById(Integer id);
    public ProductDto getProductByIdAdmin(Integer id);
}
