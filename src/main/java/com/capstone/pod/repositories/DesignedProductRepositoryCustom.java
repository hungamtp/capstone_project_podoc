package com.capstone.pod.repositories;

import com.capstone.pod.dto.designedProduct.DesignedProductDTO;

import java.util.List;

public interface DesignedProductRepositoryCustom {
    List<DesignedProductDTO> get4BestSellerDesignedProduct();
}
