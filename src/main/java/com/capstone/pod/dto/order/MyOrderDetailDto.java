package com.capstone.pod.dto.order;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Builder
public class MyOrderDetailDto {
    private String id;
    private double price;
    private String designId;
    private String designName;
    private String designImage;
    private String designerName;
    private String designerId;
    private String color;
    private String size;
    private int quantity;
}
