package com.capstone.pod.dto.cartdetail;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartDetailDTO {
    private Integer id;
    private Integer designedProductId;
    private Float price;
    private String designedProductName;
    private String designedImage;
    private String color;
    private String size;
    private int quantity;
}
