package com.capstone.pod.repositories;

import com.capstone.pod.entities.DesignedProduct;
import com.capstone.pod.repositories.impl.projection.P;

import java.util.List;

public interface DesignedProductRepositoryCustom {
    List<DesignedProduct> get4HighestRateDesignedProduct();
    List<DesignedProduct> get4HighestRateDesignedProductByProductId(String productId);
    List<DesignedProduct> get4BestSeller();
    List<DesignedProduct> get4BestSellerById(String productId);
    List<P> search(String productId) throws InterruptedException;
}
