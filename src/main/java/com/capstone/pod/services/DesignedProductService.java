package com.capstone.pod.services;

import com.capstone.pod.dto.designedProduct.DesignedProductDTO;

import java.util.List;

public interface DesignedProductService {
    List<DesignedProductDTO> get4HighestRateDesignedProduct();
    List<DesignedProductDTO> get4HighestRateDesignedProductByProductId(int productId);
}
