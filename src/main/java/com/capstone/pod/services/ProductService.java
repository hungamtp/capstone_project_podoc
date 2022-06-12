package com.capstone.pod.services;

import com.capstone.pod.dto.common.PageDTO;
import com.capstone.pod.dto.product.*;
import com.capstone.pod.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;


public interface ProductService {
      ProductDto addProduct(AddProductDto productDto);
      PageDTO getAllProducts(Specification<Product> specification, Pageable pageable);
      Page<ProductDto> getAllProductsByAdmin(Specification<Product> specification, Pageable pageable);

      ProductDto updateProduct(UpdateProductDto dto, int productId);
      ProductDto publishProduct(int productId);
      ProductDto unPublishProduct(int productId);
      ProductDto deleteProduct(int productId);

      ProductDetailDto getProductById(int id);
      ProductDto getProductByIdAdmin(int id);

      List<ProductBluePrintDto> getProductBluePrintByProductId(int productId);
}
