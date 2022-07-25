package com.capstone.pod.dto.order;

import lombok.*;

import java.time.LocalDate;

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
    private String provider;
    private String designerId;
    private String color;
    private String size;
    private LocalDate date;
    private int quantity;
    private boolean isRated;
}
