package com.capstone.pod.dto.order;

import com.capstone.pod.constant.validation_message.ValidationMessage;
import com.capstone.pod.constant.validation_size.ValidationSize;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Builder
public class CancelOrderDto {
    private List<String> orderDetailIds;
    @NotBlank(message = ValidationMessage.CANCEL_REASON_VALID_MESSAGE)
    @Size(min = ValidationSize.CANCEL_REASON_MIN,message = ValidationMessage.CANCEL_REASON_SIZE_VALID_MESSAGE)
    private String cancelReason;
}
