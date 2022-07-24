package com.capstone.pod.dto.sizeproduct;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EditSizeProductDto {
    private String id;
    private String size;
    private double width;
    private double height;
}
