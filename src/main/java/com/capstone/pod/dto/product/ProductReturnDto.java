package com.capstone.pod.dto.product;

import com.capstone.pod.dto.sizecolor.SizeColorInFactoryDetailDto;
import lombok.*;

import java.util.List;
import java.util.Set;
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductReturnDto {
    private String id;
    private String name;
    private String description;
    private String categoryName;
    private boolean isPublic;
    private boolean isDeleted;
    private List<ProductImagesDto> productImages;
}
