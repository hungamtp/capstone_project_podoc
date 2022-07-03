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
public class AddProductBluePrintDto {
    private String id;
    @NotBlank(message = ValidationMessage.IMAGE_VALID_MESSAGE)
    private String frameImage;
    @NotBlank(message = ValidationMessage.POSITION_VALID_MESSAGE)
    private String position;
    @NotNull(message = ValidationMessage.PLACEHOLER_TOP_VALID_MESSAGE)
    private double placeHolderTop;
    @NotNull(message = ValidationMessage.HEIGHT_VALID_MESSAGE)
    private double placeHolderHeight;
    @NotNull(message = ValidationMessage.WIDTH_VALID_MESSAGE)
    private double placeHolderWidth;
}
