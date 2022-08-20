package com.capstone.pod.dto.designedProduct;

import com.capstone.pod.dto.imagepreview.ImagePreviewDto;
import com.capstone.pod.dto.user.UserInDesignDto;
import lombok.*;

import java.util.List;
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Builder
public class ViewAllDesignDto {
    private String id;
    private String name;
    private boolean publish;
    private double price;
    private UserInDesignDto user;
    private List<ImagePreviewDto> imagePreviews;
    private double rating;
    private int sold;
    private int rateCount;
    private List<String> tagName;
}
