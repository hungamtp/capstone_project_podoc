package com.capstone.pod.dto.factory;

import lombok.*;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FactoryProductDetailDTO {
    private int id;
    private String name;
    private String location;
    private float price;
    private List<String> sizes;
    private List<String> area;
    private List<String> colors;
}


