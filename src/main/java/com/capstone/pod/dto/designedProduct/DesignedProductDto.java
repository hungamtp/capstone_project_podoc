package com.capstone.pod.dto.designedProduct;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DesignedProductDto {
    private String id;
    private String name;
    private String image;
    private double designedPrice;
    private double rate;
    private List<String> tags = new ArrayList<>();
    private Integer userId;
    private String username;
    private Long soldCount;
}
