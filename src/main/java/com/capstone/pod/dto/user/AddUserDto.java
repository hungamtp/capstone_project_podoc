package com.capstone.pod.dto.user;

import com.capstone.pod.constant.validation_message.ValidationMessage;
import com.capstone.pod.constant.validation_size.ValidationSize;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class AddUserDto {
    @NotBlank(message = ValidationMessage.FIRSTNAME_VALID_MESSAGE)
    private String firstName;
    @NotBlank(message = ValidationMessage.LASTNAME_VALID_MESSAGE)
    private String lastName;
    @NotBlank(message = ValidationMessage.USERNAME_VALID_MESSAGE)
    private String username;
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
    @NotBlank(message = ValidationMessage.ROLE_VALID_MESSAGE)
    private String roleName;
    private String avatar;
}
