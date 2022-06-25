package com.capstone.pod.dto.sizecolor;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@EqualsAndHashCode
public class SizeColorInFactoryDetailDto {
    private int quantity;
    private String size;
    private String color;
}
