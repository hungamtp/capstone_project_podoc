package com.capstone.pod.dto.factory;

import lombok.*;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FactoryProductDetailDto {
    private String id;
    private String name;
    private String location;
    private Double price;
    private List<String> sizes;
    private List<String> area;
    private List<String> colors;
}


