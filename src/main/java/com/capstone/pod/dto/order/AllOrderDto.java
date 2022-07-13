package com.capstone.pod.dto.order;

import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@Builder
@NoArgsConstructor
@Setter
@Getter
public class AllOrderDto {
    private String orderId;
    private double totalBill;
    private LocalDate createdDate;
    private Boolean isPaid;
    private int countItem;
}
