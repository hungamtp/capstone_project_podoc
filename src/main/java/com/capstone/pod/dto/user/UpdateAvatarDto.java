package com.capstone.pod.dto.user;

import com.capstone.pod.constant.validation_message.ValidationMessage;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class UpdateAvatarDto {
    @NotBlank(message = ValidationMessage.IMAGE_VALID_MESSAGE)
    private String image;
}
