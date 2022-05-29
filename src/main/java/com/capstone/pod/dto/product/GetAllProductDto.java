package com.capstone.pod.dto.product;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.List;

@Getter
@Setter
public class GetAllProductDto {
    private int id;
    private String name;
    private Collection<String> productImagesImage;
    private String categoryName;
    private String productTagsTagName;
    private List<Double> priceByFactoriesPrice;
    private int numberOfSize;
    private int numberOfColor;
    private int numberOfFactory;
}
