package com.capstone.pod.dto.product;

import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.List;

@Getter
@Setter
public class GetAllProductDto {
    private int id;
    private String name;
    private String description;
    private Collection<ProductImagesDto> productImages;
    private String categoryName;
    private List<ProductTagDto> productTags;
    private List<PriceByFactoryDto> priceByFactories;
    private int numberOfSize;
    private int numberOfColor;
    private int numberOfFactory;
}
