package com.capstone.pod.dto.designedProduct;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class DesignedProductDetailDTO {
    private Integer id;
    private String name;
    private String image;
    private Double designedPrice;
    private Double rate;
    private String tag;
    private Integer userId;
    private String username;
    private Long soldCount;

}


