package com.capstone.pod.dto.product;

import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
@Setter
@Getter
public class ProductDto {
    private int id;
    private String name;
    private Collection<String> productImagesImage;
    private String categoryName;
    private String productTagsTagName;
    private Double priceByFactoriesPrice;
    private int numberOfSize;
    private int numberOfColor;
    private int numberOfFactory;
}
