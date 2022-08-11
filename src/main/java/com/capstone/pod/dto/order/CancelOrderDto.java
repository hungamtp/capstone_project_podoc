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
    private String orderDetailId;
    @NotBlank(message = ValidationMessage.CANCEL_REASON_VALID_MESSAGE)
    private String cancelReason;
}
