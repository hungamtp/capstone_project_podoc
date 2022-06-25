package com.capstone.pod.dto.sizecolor;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class SizeColorInFactoryDetailDto {
    private int quantity;
    private String size;
    private String color;
}
