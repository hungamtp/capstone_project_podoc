package com.capstone.pod.dto.factory;

import com.capstone.pod.constant.validation_message.ValidationMessage;
import lombok.*;

import javax.validation.constraints.NotNull;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AddPriceByFactoryDto {
    @NotNull(message = ValidationMessage.PRICE_VALID_MESSAGE)
    private double price;
    @NotNull(message = ValidationMessage.MATERIAL_VALID_MESSAGE)
    private String material;
}
