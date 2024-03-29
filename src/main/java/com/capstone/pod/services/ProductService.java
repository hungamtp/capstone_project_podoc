package com.capstone.pod.services;

import com.capstone.pod.dto.color.ColorInDesignDto;
import com.capstone.pod.dto.common.PageDTO;
import com.capstone.pod.dto.product.*;
import com.capstone.pod.dto.sizecolor.SizeColorByProductIdDto;
import com.capstone.pod.dto.sizecolor.SizeColorInRawProductDto;
import com.capstone.pod.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;


public interface ProductService {
      ProductDto addProduct(AddProductDto productDto);
      PageDTO getAllProducts(Specification<Product> specification, Pageable pageable);
      Page<ProductByAdminDto> getAllProductsByAdmin(Specification<Product> specification, Pageable pageable);

      ProductReturnDto updateProduct(UpdateProductDto dto, String productId);
      ProductReturnDto publishProduct(String productId);
      ProductReturnDto unPublishProduct(String productId);
      ProductReturnDto deleteProduct(String productId);

      ProductDetailDto getProductById(String id);
      ProductByAdminDto getProductByIdAdmin(String id);

      List<ProductBluePrintDto> getProductBluePrintByProductId(String productId);

      SizeColorByProductIdDto getSizesAndColorByProductId(String productId);
      List<ColorInDesignDto> getColorsByProductNameAndFactoryName(String productId, String factoryId);

      List<GetProductFactoryDto> getAllProductForFactoryDoNotHaveYet(String factoryId, String productName);

      AddProductBluePrintDto addProductBluePrint(String productId, AddProductBluePrintDto addProductBluePrintDto);
      EditProductBluePrintDto editProductBluePrint(String productBlueprintId, EditProductBluePrintDto dto);

      List<SizeColorInRawProductDto> getColorSizeMapByColorByProductId(String productId);

}
