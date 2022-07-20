package com.capstone.pod.dto.order;

import com.capstone.pod.dto.blueprint.BluePrintDto;
import lombok.*;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Builder
public class OrderDetailForPrintingDto {
    private String orderId;
    private List<String> previewImages;
    private String createDate;
    private List<BluePrintDto> bluePrintDtos;
    private List<OrderDetailsSupportDto> orderDetailsSupportDtos;
    private String productId;
    private String status;
}
