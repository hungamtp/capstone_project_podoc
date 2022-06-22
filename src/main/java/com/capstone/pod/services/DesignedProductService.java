package com.capstone.pod.services;

import com.capstone.pod.dto.designedProduct.*;
import com.capstone.pod.entities.DesignedProduct;
import com.capstone.pod.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface DesignedProductService {
    List<DesignedProductDto> get4HighestRateDesignedProduct();
    List<DesignedProductDto> get4HighestRateDesignedProductByProductId(int designId);
    DesignedProductReturnDto addDesignedProduct(DesignedProductSaveDto dto, int productId, int factoryId);
    DesignedProductReturnDto editDesignedProduct(DesignedProductSaveDto dto, int designId);
    DesignedProductReturnDto publishDesignedProduct(int designId);
    DesignedProductReturnDto getDesignedProductById(int designId);
    DesignedProductReturnDto unPublishDesignedProduct(int designId);
    DesignedProductReturnDto editDesignedProductPrice(DesignedProductPriceDto dto, int designId);
    Page<ViewMyDesignDto> viewMyDesign(Pageable page);
    Page<ViewAllDesignDto> viewAllDesign(Specification<DesignedProduct> specification, Pageable page);
    Page<ViewOtherDesignDto> viewOtherDesign(Pageable page, int userId);
    ViewOtherDesignDto viewDesignDetailsByDesignId(int designId);
    void deleteDesignedProduct(int designedProductId);
}
