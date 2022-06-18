package com.capstone.pod.dto.order;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Builder
public class OrderDto {
    private int id;
    private double totalPrice;
    private String address;
    private String phone;
    private String customerName;
    private String userId;
    private String orderStatusStatusName;
}
