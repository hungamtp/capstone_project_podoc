package com.capstone.pod.dto.designedProduct;

import com.capstone.pod.dto.imagepreview.ImagePreviewDto;
import com.capstone.pod.dto.user.UserDto;
import com.capstone.pod.dto.user.UserInDesignDto;
import lombok.*;

import java.util.List;
import java.util.Set;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Builder
public class ViewOtherDesignDto {
    private int id;
    private String name;
    private boolean publish;
    private double price;
    private UserInDesignDto user;
    private List<ImagePreviewDto> imagePreviews;
    private double rating;
    private int sold;
    private List<String> tagName;
    private Set colors;
    private Set sizes;
    private String description;
    private String factoryName;
}
