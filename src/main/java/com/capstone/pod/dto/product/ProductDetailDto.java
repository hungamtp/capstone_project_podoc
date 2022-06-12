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
public class ProductDetailDto {
    private int id;
    private String name;
    private String description;
    private Double lowestPrice;
    private Double highestPrice;
    private List<String> images;
    private Collection<ProductImagesDto> productImages;
    private String categoryName;
    private List<String> tags;
    private List<FactoryProductDetailDTO> factories;

}
