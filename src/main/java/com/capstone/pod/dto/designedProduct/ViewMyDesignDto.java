package com.capstone.pod.dto.designedProduct;

import com.capstone.pod.dto.imagepreview.ImagePreviewDto;
import com.capstone.pod.dto.user.UserInDesignDto;
import lombok.*;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
public class ViewMyDesignDto {
    private String id;
    private String name;
    private Long soldCount;
    private boolean publish;
    private double designedPrice;
    private List<ImagePreviewDto> imagePreviews;
    private boolean isProductOfDesignDeleted;
}
