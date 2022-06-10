package com.capstone.pod.dto.designedProduct;

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
public class ViewOtherDesignDto {
    private int id;
    private String name;
    private boolean publish;
    private double designedPrice;
    private List<ImagePreviewDto> imagePreviews;
}
