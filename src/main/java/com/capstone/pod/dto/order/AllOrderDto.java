package com.capstone.pod.dto.order;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Builder
@NoArgsConstructor
@Setter
@Getter
public class AllOrderDto {
    private String orderId;
    private double totalBill;
    private LocalDateTime createdDate;
    private Boolean isPaid;
    private int countItem;
    private List<MyOrderDetailDto> orderDetailDtos;
}
