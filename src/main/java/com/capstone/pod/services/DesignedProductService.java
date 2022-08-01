package com.capstone.pod.services;

import com.capstone.pod.dto.common.PageDTO;
import com.capstone.pod.dto.designedProduct.*;
import com.capstone.pod.entities.DesignedProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface DesignedProductService {
    List<DesignedProductDto> get4HighestRateDesignedProduct();
    List<DesignedProductDto> get4BestSeller();
    List<DesignedProductDto> get4BestSellerById(String productId);
    List<DesignedProductDto> get4HighestRateDesignedProductByProductId(String designId);
    DesignedProductReturnDto addDesignedProduct(DesignedProductSaveDto dto, String productId, String factoryId);
    DesignedProductReturnDto editDesignedProduct(DesignedProductSaveDto dto, String designId);
    DesignedProductReturnDto getDesignedProductById(String designId);
    DesignedProductReturnDto unPublishDesignedProduct(String designId);
    DesignedProductReturnDto editDesignedProductPrice(DesignedProductPriceDto dto, String designId);
    Page<ViewMyDesignDto> viewMyDesign(Pageable page);
    PageDTO viewAllDesign(Specification<DesignedProduct> specification, Pageable page);
    Page<ViewOtherDesignDto> viewOtherDesign(Pageable page, String userId);
    ViewOtherDesignDto viewDesignDetailsByDesignId(String designId);
    void deleteDesignedProduct(String designedProductId);
    boolean publishOrUnpublishDesign(String designId , String email ,boolean publish);
}
