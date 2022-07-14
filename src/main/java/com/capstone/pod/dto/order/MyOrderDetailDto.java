package com.capstone.pod.dto.order;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class MyOrderDetailDto {
    private String id;
    private String name;
    private String designer;
    private String size;
    private String color;
    private double price;
    private int quantity;
}
