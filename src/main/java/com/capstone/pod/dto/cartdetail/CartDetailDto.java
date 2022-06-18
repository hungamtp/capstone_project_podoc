package com.capstone.pod.dto.cartdetail;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartDetailDto {
    private Integer id;
    private Integer designedProductId;
    private Float price;
    private String designedProductName;
    private String designedImage;
    private String color;
    private String size;
    private int quantity;
}
