package com.capstone.pod.dto.factory;

import com.capstone.pod.constant.validation_message.ValidationMessage;
import com.capstone.pod.constant.validation_size.ValidationSize;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
@Getter
@Setter
public class AddFactoryDto {
    @NotBlank(message = ValidationMessage.NAME_VALID_MESSAGE)
    private String name;
    @NotBlank(message = ValidationMessage.PASSWORD_VALID_MESSAGE)
    private String password;
    @NotBlank(message = ValidationMessage.ADDRESS_VALID_MESSAGE)
    @Size(min = ValidationSize.ADDRESS_MIN,message = ValidationMessage.ADDRESS_SIZE_VALID_MESSAGE)
    private String address;
    @Size(min = ValidationSize.PHONE_MIN,max = ValidationSize.PHONE_MAX,message = ValidationMessage.PHONE_SIZE_VALID_MESSAGE)
    @NotBlank(message = ValidationMessage.PHONE_NOT_EMPTY_VALID_MESSAGE)
    private String phone;
    @Email(message = ValidationMessage.EMAIL_VALID_MESSAGE)
    private String email;
    @NotBlank(message = ValidationMessage.LOGO_VALID_MESSAGE)
    private String logo;
}
