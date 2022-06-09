package com.capstone.pod.services;

import com.capstone.pod.constant.role.RolePreAuthorize;
import com.capstone.pod.dto.designedProduct.DesignedProductDTO;
import com.capstone.pod.dto.designedProduct.DesignedProductReturnDto;
import com.capstone.pod.dto.designedProduct.DesignedProductSaveDto;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

public interface DesignedProductService {
    List<DesignedProductDTO> get4HighestRateDesignedProduct();
    List<DesignedProductDTO> get4HighestRateDesignedProductByProductId(int productId);

    DesignedProductReturnDto addDesignedProduct(DesignedProductSaveDto dto, int productId);
}
