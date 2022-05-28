package com.capstone.pod.dto.auth;

import com.capstone.pod.constant.validation_message.ValidationMessage;
import com.capstone.pod.constant.validation_size.ValidationSize;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Builder
public class RegisterDto {
    @NotBlank(message = ValidationMessage.PASSWORD_VALID_MESSAGE)
    @Size(min = ValidationSize.PASSWORD_MIN,message = ValidationMessage.PASSWORD_SIZE_VALID_MESSAGE)
    private String password;
    @Email(message = ValidationMessage.EMAIL_VALID_MESSAGE)
    private String email;
    @NotBlank(message = ValidationMessage.ADDRESS_VALID_MESSAGE)
    private String address;
    @NotBlank(message = ValidationMessage.PHONE_NOT_EMPTY_VALID_MESSAGE)
    @Size(min = ValidationSize.PHONE_MIN,max = ValidationSize.PHONE_MAX, message = ValidationMessage.PHONE_SIZE_VALID_MESSAGE)
    private String phone;
    private String image;
}
