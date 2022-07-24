package com.capstone.pod.dto.sizeproduct;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddSizeProductDto {
    private String size;
    private double width;
    private double height;
    private String productId;
}
