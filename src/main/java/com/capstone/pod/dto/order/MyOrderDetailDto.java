package com.capstone.pod.dto.order;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

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
    private String status;
    private LocalDateTime date;
    private int quantity;
    private boolean isRated;
    private boolean isCancel;
    private String reasonByUser;
    private String reasonByFactory;
    List<OrderStateDto> statuses;
}
