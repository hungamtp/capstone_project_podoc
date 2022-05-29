package com.capstone.pod.dto.product;

import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.List;

@Setter
@Getter
public class ProductDto {
    private int id;
    private String name;
    private Collection<ProductImagesDto> productImages;
    private String categoryName;
    private String productTagsTagName;
    private List<ProductTagDto> productTags;
    private List<PriceByFactoryDto> priceByFactories;
}
