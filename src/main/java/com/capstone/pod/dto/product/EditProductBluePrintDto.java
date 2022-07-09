package com.capstone.pod.dto.product;

import com.capstone.pod.constant.validation_message.ValidationMessage;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EditProductBluePrintDto {
    private String id;
    @NotBlank(message = ValidationMessage.IMAGE_VALID_MESSAGE)
    private String frameImage;
    @NotNull(message = ValidationMessage.PLACEHOLER_TOP_VALID_MESSAGE)
    private double placeHolderTop;
    @NotNull(message = ValidationMessage.HEIGHT_VALID_MESSAGE)
    private double placeHolderHeight;
    @NotNull(message = ValidationMessage.WIDTH_VALID_MESSAGE)
    private double placeHolderWidth;
    @NotNull(message = ValidationMessage.WIDTH_RATE_VALID_MESSAGE)
    private double widthRate;
    @NotNull(message = ValidationMessage.HEIGHT_RATE_VALID_MESSAGE)
    private double heightRate;
}
