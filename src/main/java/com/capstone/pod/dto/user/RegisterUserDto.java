package com.capstone.pod.dto.user;

import com.capstone.pod.constant.validation_message.ValidationMessage;
import com.capstone.pod.constant.validation_size.ValidationSize;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Builder
public class RegisterUserDto {

    @NotBlank(message = ValidationMessage.FIRSTNAME_VALID_MESSAGE)
    private String firstName;
    @NotBlank(message = ValidationMessage.LASTNAME_VALID_MESSAGE)
    private String lastName;
    @NotBlank(message = ValidationMessage.PASSWORD_VALID_MESSAGE)
    @Size(min = ValidationSize.PASSWORD_MIN,message = ValidationMessage.PASSWORD_SIZE_VALID_MESSAGE)
    private String password;
    @Email(message = ValidationMessage.EMAIL_VALID_MESSAGE)
    private String email;
    private String address;
    private String phone;
    private String avatar;
}
