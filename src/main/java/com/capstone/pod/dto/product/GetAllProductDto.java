package com.capstone.pod.dto.product;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@Builder
public class GetAllProductDto {
    private int id;
    private String name;
    private List<String> productImages= new ArrayList<>();
    private String categoryName;
    private List<String> tags = new ArrayList<>();
    private int numberOfSize;
    private int numberOfColor;
    private int numberOfFactory;
}
