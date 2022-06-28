package com.capstone.pod.dto.product;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class GetProductFactoryDto {
    private int id;
    private String name;
}
