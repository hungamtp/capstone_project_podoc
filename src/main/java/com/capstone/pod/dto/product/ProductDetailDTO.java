package com.capstone.pod.dto.product;

import com.capstone.pod.dto.factory.FactoryProductDetailDTO;
import lombok.*;

import java.util.Collection;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductDetailDTO {
    private int id;
    private String name;
    private String description;
    private Double lowestPrice;
    private Double highestPrice;
    private List<String> images;
    private String categoryName;
    private List<String> tags;
    private List<FactoryProductDetailDTO> factories;

}
