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
    private int id;
    private String name;
    private String image;
    private double designedPrice;
    private double rate;
    private String tag;

}


