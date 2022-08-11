package com.capstone.pod.dto.order;

import com.capstone.pod.constant.validation_message.ValidationMessage;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Builder
public class CancelOrderDto {
    @NotBlank(message = ValidationMessage.ID_VALID_MESSAGE)
    private String orderId;
    @NotBlank(message = ValidationMessage.CANCEL_REASON_VALID_MESSAGE)
    private String cancelReason;
}
