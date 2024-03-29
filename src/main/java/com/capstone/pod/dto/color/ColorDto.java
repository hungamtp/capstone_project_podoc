package com.capstone.pod.dto.color;

import com.capstone.pod.constant.validation_message.ValidationMessage;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ColorDto {
    private String id;
    @NotBlank(message = ValidationMessage.NAME_VALID_MESSAGE)
    private String name;
    @NotBlank(message = ValidationMessage.COLOR_VALID_MESSAGE)
    private String imageColor;
}
