package com.capstone.pod.dto.cartdetail;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartDetailDto {
    private String id;
    private String cartId;
    private String designedProductId;
    private Float price;
    private String designedProductName;
    private String designedImage;
    private String color;
    private String size;
    private int quantity;
    private boolean publish;
}
