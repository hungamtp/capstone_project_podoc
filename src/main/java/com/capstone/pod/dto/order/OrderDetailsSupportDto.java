package com.capstone.pod.dto.order;

import lombok.*;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Builder
public class OrderDetailsSupportDto {
    private String orderDetailsId;
    private String color;
    private String colorImage;
    private String size;
    private int quantity;
    private boolean canceled;
    private String reasonByUser;
    private String reasonByFactory;
    private boolean isRate;
    private String status;
    private double price;
    private LocalDate createdDate;
}
