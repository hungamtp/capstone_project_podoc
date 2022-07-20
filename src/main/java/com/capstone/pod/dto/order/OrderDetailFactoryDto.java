package com.capstone.pod.dto.order;

import lombok.*;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Builder
public class OrderDetailFactoryDto {
    private String id;
    private String productName;
    private String designName;
    private String designId;
    private String designedImage;
    private String color;
    private String size;
    private double price;
    private int quantity;
    private String status;
}
