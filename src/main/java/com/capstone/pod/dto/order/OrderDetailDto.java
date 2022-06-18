package com.capstone.pod.dto.order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class OrderDetailDto {
    private int id;
    private double designedPrice;
    private double priceFromFactory;
    private int designedProductId;
    private String designedProductName;
    private String designedImage;
    private String color;
    private String size;
    private int quantity;
}
