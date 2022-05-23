package com.capstone.pod.dto.product;

import java.util.Collection;

public class AddProductDto {
    private int id;
    private String name;
    private double price;
    private boolean isPublic;
    private String description;
    private boolean isDeleted;
    private Collection<String> productImagesImage;
    private String categoryName;
}
