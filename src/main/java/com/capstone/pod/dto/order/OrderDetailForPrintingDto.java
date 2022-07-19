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
    private String id;
    private String orderId;
    private List<String> previewImages;
    private String createDate;
    private List<BluePrintDto> bluePrintDtos;
    private String productId;
    private String color;
    private String size;
    private double price;
    private int quantity;
    private String status;
}
