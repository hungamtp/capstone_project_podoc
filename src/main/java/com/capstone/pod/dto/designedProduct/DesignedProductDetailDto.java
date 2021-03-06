package com.capstone.pod.dto.designedProduct;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class DesignedProductDetailDto {
    private String id;
    private String name;
    private String image;
    private Double designedPrice;
    private Double rate;
    private String tag;
    private String userId;
    private String username;
    private Long soldCount;

}


