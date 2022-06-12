package com.capstone.pod.dto.product;

import lombok.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetAllProductDto {
    private int id;
    private String name;
    private List<ProductImagesDto> productImages;
    private String categoryName;
    private List<ProductTagDto> tags ;
    private int numberOfSize;
    private int numberOfColor;
    private int numberOfFactory;
    private Double priceFrom;
}
