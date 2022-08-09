package com.capstone.pod.dto.designedProduct;

import com.capstone.pod.dto.color.ColorInDesignDto;
import com.capstone.pod.dto.imagepreview.ImagePreviewDto;
import com.capstone.pod.dto.sizecolor.SizeColorDesignedAndFactorySellDto;
import com.capstone.pod.dto.user.UserInDesignDto;
import lombok.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Builder
public class ViewOtherDesignDto {
    private String id;
    private String name;
    private String material;
    private boolean publish;
    private double price;
    private UserInDesignDto user;
    private List<ImagePreviewDto> imagePreviews;
    private double rating;
    private int sold;
    private int rateCount;
    private List<String> tagName;
    private Set<ColorInDesignDto> colors;
    private Set sizes;
    private String description;
    private String factoryName;
    Map<String, List<SizeColorDesignedAndFactorySellDto>> colorAndSizes;
}
