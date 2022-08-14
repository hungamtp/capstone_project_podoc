package com.capstone.pod.dto.order;

import com.capstone.pod.dto.blueprint.BluePrintDto;
import com.capstone.pod.dto.imagepreview.ImagePreviewDto;
import lombok.*;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Builder
public class OrderDetailForPrintingDto {
    private String customerName;
    private String email;
    private String phoneNumber;
    private String address;
    private String orderId;
    private List<ImagePreviewDto> previewImages;
    private String createDate;
    private List<BluePrintDto> bluePrintDtos;
    private List<OrderDetailsSupportDto> orderDetailsSupportDtos;
    private String productId;
    private List<String> statuses;
    private String status;
    private boolean isCanceled;
    private String cancelReason;
}
