package com.capstone.pod.dto.designedProduct;

import com.capstone.pod.constant.validation_message.ValidationMessage;
import com.capstone.pod.dto.blueprint.BluePrintDto;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DesignedProductSaveDto {
    @NotNull
    private List<String> imagePreviews;
    @NotNull(message = ValidationMessage.DESIGNED_PRODUCT_PRICE_VALID_MESSAGE)
    private double designedPrice;
    @NotNull(message = ValidationMessage.DESIGNED_PRODUCT_VALID_MESSAGE)
    private List<BluePrintDto> bluePrintDtos;
}
