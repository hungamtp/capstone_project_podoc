package com.capstone.pod.dto.order;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Builder
public class CancelOrderDto {
    private String orderDetailId;
    private String cancelReason;
}
