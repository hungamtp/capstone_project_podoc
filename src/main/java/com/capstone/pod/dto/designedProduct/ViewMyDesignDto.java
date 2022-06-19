package com.capstone.pod.dto.designedProduct;

import com.capstone.pod.dto.imagepreview.ImagePreviewDto;
import com.capstone.pod.dto.user.UserInDesignDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class ViewMyDesignDto {
    private int id;
    private String name;
    private boolean publish;
    private double designedPrice;
    private UserInDesignDto user;
    private List<ImagePreviewDto> imagePreviews;
}
