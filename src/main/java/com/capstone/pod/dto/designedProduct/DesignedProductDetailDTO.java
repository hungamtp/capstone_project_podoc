package com.capstone.pod.dto.designedProduct;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DesignedProductDetailDTO {
    private Integer id;
    private String name;
    private String image;
    private Double designedPrice;
    private Double rate;
    private String tag;

}


