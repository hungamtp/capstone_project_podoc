package com.capstone.pod.dto.product;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
public class GetProductFactoryDto {
    private String id;
    private String name;
    private LocalDateTime createDate;
}
