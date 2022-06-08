package com.capstone.pod.dto.designedProduct;

import com.capstone.pod.constant.validation_message.ValidationMessage;
import com.capstone.pod.dto.blueprint.BluePrintDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@Builder
public class DesignedProductSaveDto {
    @NotBlank(message = ValidationMessage.DESIGNED_PRODUCT_NAME_VALID_MESSAGE)
    private String name;
    @NotBlank(message = ValidationMessage.DESIGNED_PRODUCT_PRICE_VALID_MESSAGE)
    private double designedPrice;
    @NotNull(message = ValidationMessage.DESIGNED_PRODUCT_VALID_MESSAGE)
    private List<BluePrintDto> bluePrintDtos;
}
