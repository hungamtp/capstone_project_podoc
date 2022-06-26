package com.capstone.pod.dto.product;

import lombok.*;

import java.util.Collection;
import java.util.List;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductByAdminDto {
    private int id;
    private String name;
    private String description;
    private boolean isPublic;
    private boolean isDeleted;
    private Collection<ProductImagesDto> productImages;
    private String categoryName;
    private List<ProductTagDto> productTags;
    private List<PriceByFactoryDto> priceByFactories;
}
