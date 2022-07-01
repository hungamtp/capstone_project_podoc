package com.capstone.pod.dto.product;

import com.capstone.pod.dto.sizecolor.SizeColorDto;
import com.capstone.pod.dto.sizecolor.SizeColorInFactoryDetailDto;
import lombok.*;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {
    private String id;
    private String name;
    private String description;
    private boolean isPublic;
    private boolean isDeleted;
    private List<ProductImagesDto> productImages;
    private String categoryName;
    private Set<SizeColorInFactoryDetailDto> sizeColors;
}
