package com.capstone.pod.repositories;

import com.capstone.pod.dto.designedProduct.DesignedProductDetailDto;
import com.capstone.pod.entities.DesignedProduct;

import java.util.List;

public interface DesignedProductRepositoryCustom {
    List<DesignedProduct> get4HighestRateDesignedProduct();
    List<DesignedProduct> get4HighestRateDesignedProductByProductId(String productId);
    List<DesignedProduct> get4BestSeller();
    List<DesignedProduct> get4BestSellerById(String productId);
}
