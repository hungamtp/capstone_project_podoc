package com.capstone.pod.dto.product;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Setter
@Getter
public class UpdateProductDto {
    private String name;
    private String description;
    private List<String> images;
    private String categoryName;
}
