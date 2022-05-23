package com.capstone.pod.dto.product;

import lombok.Data;

import java.util.Collection;
@Data
public class GetProductByIdDto {
    private int id;
    private String name;
    private double price;
    private boolean isPublic;
    private String description;
    private boolean isDeleted;
    private Collection<String> productImagesImage;
    private String categoryName;
}
