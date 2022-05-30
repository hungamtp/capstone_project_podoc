package com.capstone.pod.dto.designedProduct;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DesignedProductDTO {
    private int id;
    private String name;
    private String image;
    private double designedPrice;
    private double rate;
    private ArrayList<String> tags;

    public DesignedProductDTO(int id, String name, String image, double designedPrice, double rate) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.designedPrice = designedPrice;
        this.rate = rate;
    }
}


