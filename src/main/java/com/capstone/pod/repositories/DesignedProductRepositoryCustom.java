package com.capstone.pod.repositories;

import com.capstone.pod.dto.designedProduct.DesignedProductDetailDTO;

import java.util.List;

public interface DesignedProductRepositoryCustom {
    List<DesignedProductDetailDTO> get4HighestRateDesignedProduct();
    List<DesignedProductDetailDTO> get4HighestRateDesignedProductByProductId(int productId);
}
