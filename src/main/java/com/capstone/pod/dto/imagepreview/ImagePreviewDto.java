package com.capstone.pod.dto.imagepreview;

import com.capstone.pod.constant.validation_message.ValidationMessage;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Builder
public class ImagePreviewDto {
    @NotBlank(message = ValidationMessage.IMAGE_VALID_MESSAGE)
    private String image;
    @NotBlank(message = ValidationMessage.POSITION_VALID_MESSAGE)
    private String position;
    private String color;
}
