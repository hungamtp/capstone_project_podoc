package com.capstone.pod.dto.product;

import lombok.Data;

import java.util.Collection;
import java.util.List;

@Data
public class GetProductByIdDto {
    private int id;
    private String name;
    private boolean isPublic;
    private String description;
    private Collection<String> productImagesImage;
    private String categoryName;
    private List<String> productTagsTagName;
    private List<Double> priceByFactoriesPrice;
}
