package com.capstone.pod.dto.designedProduct;

import com.capstone.pod.dto.blueprint.BluePrintDto;
import com.capstone.pod.dto.color.ColorDto;
import com.capstone.pod.dto.color.ColorInDesignDto;
import com.capstone.pod.dto.imagepreview.ImagePreviewDto;
import lombok.*;

import java.util.List;
import java.util.Set;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Builder
public class DesignedProductReturnDto {
    private String id;
    private String name;
    private String description;
    private String productName;
    private String factoryName;
    private String material;
    private String factoryId;
    private String productId;
    private boolean publish;
    private double designedPrice;
    private double priceFromFactory;
    private List<String> colors;
    private Set<ColorInDesignDto> colorsObj;
    private List<ImagePreviewDto> imagePreviews;
    private List<BluePrintDto> bluePrints;
}
