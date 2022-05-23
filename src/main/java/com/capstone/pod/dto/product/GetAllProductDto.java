package com.capstone.pod.dto.product;

import lombok.Data;

import java.util.Collection;

@Data
public class GetAllProductDto {
    private int id;
    private String name;
    private double price;
    private Collection<String> productImagesImage;
    private String categoryName;
}
