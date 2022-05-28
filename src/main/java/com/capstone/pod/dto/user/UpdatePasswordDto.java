package com.capstone.pod.dto.user;

import com.capstone.pod.constant.validation_message.ValidationMessage;
import com.capstone.pod.constant.validation_size.ValidationSize;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class UpdatePasswordDto {
    @NotBlank(message = ValidationMessage.PASSWORD_VALID_MESSAGE)
    @Size(min= ValidationSize.PASSWORD_MIN,max = ValidationSize.PASSWORD_MAX,message = ValidationMessage.PASSWORD_SIZE_VALID_MESSAGE)
    private String newPassword;
    @NotBlank(message = ValidationMessage.PASSWORD_VALID_MESSAGE)
    @Size(min= ValidationSize.PASSWORD_MIN,max = ValidationSize.PASSWORD_MAX,message = ValidationMessage.PASSWORD_SIZE_VALID_MESSAGE)
    private String oldPassword;
}
