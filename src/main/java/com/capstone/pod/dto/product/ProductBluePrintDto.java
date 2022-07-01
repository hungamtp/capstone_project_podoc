package com.capstone.pod.dto.product;

import com.capstone.pod.dto.blueprint.BluePrintDto;
import com.capstone.pod.dto.placeholder.PlaceHolderDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ProductBluePrintDto {
    private String id;
    private String frameImage;
    private String position;
    private PlaceHolderDto placeholder;
}
