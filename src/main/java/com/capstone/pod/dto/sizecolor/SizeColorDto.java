package com.capstone.pod.dto.sizecolor;

import com.capstone.pod.constant.validation_message.ValidationMessage;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SizeColorDto {
    @NotNull(message = ValidationMessage.PRICE_VALID_MESSAGE)
    private List<String> sizes;
    @NotNull(message = ValidationMessage.COLOR_VALID_MESSAGE)
    private List<String> colors;
}
