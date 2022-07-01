package com.capstone.pod.dto.product;

import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.List;
@Setter
@Getter
public class GetDetailsProductDto {
    private String id;
    private String name;
    private String description;
    private boolean isPublic;
    private boolean isDeleted;
    private Collection<ProductImagesDto> productImages;
    private String categoryName;
    private List<ProductTagDto> productTags;
    private List<PriceByFactoryDto> priceByFactories;
}
