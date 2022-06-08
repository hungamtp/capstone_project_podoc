package com.capstone.pod.dto.placeholder;

import com.capstone.pod.constant.validation_message.ValidationMessage;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlaceHolderDto {
    @NotBlank(message=ValidationMessage.WIDTH_VALID_MESSAGE)
    private double width;
    @NotBlank(message=ValidationMessage.HEIGHT_VALID_MESSAGE)
    private double height;
}
