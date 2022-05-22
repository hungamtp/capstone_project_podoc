package com.capstone.pod.dto.user;

import com.capstone.pod.constant.validation_message.ValidationMessage;
import lombok.Data;

import javax.validation.constraints.Email;
@Data
public class UpdateEmailDto {
    @Email(message = ValidationMessage.EMAIL_VALID_MESSAGE)
    private String email;
}
