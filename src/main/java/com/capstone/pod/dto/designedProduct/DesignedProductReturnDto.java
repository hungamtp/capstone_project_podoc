package com.capstone.pod.dto.designedProduct;

import com.capstone.pod.dto.blueprint.BluePrintDto;
import com.capstone.pod.dto.color.ColorDto;
import com.capstone.pod.dto.imagepreview.ImagePreviewDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class DesignedProductReturnDto {
    private int id;
    private String name;
    private boolean publish;
    private double designedPrice;
    private List<ImagePreviewDto> imagePreviews;
    private List<BluePrintDto> bluePrints;
    private List<String> colorsName;
}
