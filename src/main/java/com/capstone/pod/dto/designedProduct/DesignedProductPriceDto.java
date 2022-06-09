package com.capstone.pod.dto.designedProduct;

import com.capstone.pod.constant.validation_message.ValidationMessage;
import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DesignedProductPriceDto {
    @NotNull(message = ValidationMessage.PRICE_VALID_MESSAGE)
    private double price;
}
