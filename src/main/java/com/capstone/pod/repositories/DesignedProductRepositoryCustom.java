package com.capstone.pod.repositories;

import com.capstone.pod.dto.designedProduct.DesignedProductDetailDto;

import java.util.List;

public interface DesignedProductRepositoryCustom {
    List<DesignedProductDetailDto> get4HighestRateDesignedProduct();
    List<DesignedProductDetailDto> get4HighestRateDesignedProductByProductId(String productId);
}
