package com.capstone.pod.dto.product;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class GetProductFactoryDto {
    private String id;
    private String name;
}
