package com.capstone.pod.dto.order;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Builder
@NoArgsConstructor
@Setter
@Getter
public class AllOrderDto {
    private String orderId;
    private String cancelReasonByUser;
    private double totalBill;
    private LocalDateTime createdDate;
    private Boolean isPaid;
    private boolean canCancel;
    private boolean canceled;
    private int countItem;
    private List<MyOrderDetailDto> orderDetailDtos;
}
