package com.capstone.pod.services;

import com.capstone.pod.dto.designedProduct.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DesignedProductService {
    List<DesignedProductDTO> get4HighestRateDesignedProduct();
    List<DesignedProductDTO> get4HighestRateDesignedProductByProductId(int designId);
    DesignedProductReturnDto addDesignedProduct(DesignedProductSaveDto dto, int productId, int factoryId);
    DesignedProductReturnDto editDesignedProduct(DesignedProductSaveDto dto, int designId);
    DesignedProductReturnDto publishDesignedProduct(int designId);
    DesignedProductReturnDto getDesignedProductById(int designId);
    DesignedProductReturnDto unPublishDesignedProduct(int designId);
    DesignedProductReturnDto editDesignedProductPrice(DesignedProductPriceDto dto, int designId);
    Page<ViewOtherDesignDto> viewDesignOfOthersByUserId(Pageable page, int userId);
    void deleteDesignedProduct(int designedProductId);
}
