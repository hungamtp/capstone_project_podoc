package com.capstone.pod.dto.designedProduct;

import com.capstone.pod.dto.blueprint.BluePrintDto;
import com.capstone.pod.dto.color.ColorDto;
import com.capstone.pod.dto.imagepreview.ImagePreviewDto;
import lombok.*;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Builder
public class DesignedProductReturnDto {
    private int id;
    private String name;
    private String description;
    private boolean publish;
    private double designedPrice;
    private List<String> colors;
    private List<ImagePreviewDto> imagePreviews;
    private List<BluePrintDto> bluePrints;
}
