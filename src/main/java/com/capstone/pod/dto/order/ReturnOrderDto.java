package com.capstone.pod.dto.order;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ReturnOrderDto {
    private int id;
    private String transactionId;
    private double price;
    private String address;
    private String phone;
    private String customerName;
}
