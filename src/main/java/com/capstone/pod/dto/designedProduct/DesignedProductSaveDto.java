package com.capstone.pod.dto.designedProduct;

import com.capstone.pod.constant.validation_message.ValidationMessage;
import com.capstone.pod.dto.blueprint.BluePrintDto;
import com.capstone.pod.dto.imagepreview.ImagePreviewDto;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DesignedProductSaveDto {
    @NotNull
    private List<ImagePreviewDto> imagePreviews;
    @NotNull(message = ValidationMessage.DESIGNED_PRODUCT_PRICE_VALID_MESSAGE)
    private double designedPrice;
    @NotBlank(message = ValidationMessage.NAME_VALID_MESSAGE)
    private String name;
    @NotNull(message = ValidationMessage.COLOR_VALID_MESSAGE)
    private List<String> colors;
    @NotBlank(message = ValidationMessage.PRODUCT_DESCRIPTION_VALID_MESSAGE)
    private String description;
    @NotNull(message = ValidationMessage.DESIGNED_PRODUCT_VALID_MESSAGE)
    private List<BluePrintDto> bluePrintDtos;
}
