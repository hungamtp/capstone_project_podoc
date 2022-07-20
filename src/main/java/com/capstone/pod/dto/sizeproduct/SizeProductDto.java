package com.capstone.pod.dto.sizeproduct;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SizeProductDto {
    private String id;
    private String size;
    private int width;
    private int height;
}
