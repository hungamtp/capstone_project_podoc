package com.capstone.pod.dto.cartdetail;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartDetailDTO {
    private Integer id;
    private Integer designedProductId;
    private String designedProductName;
    private String color;
    private String size;
    private int quantity;
}
