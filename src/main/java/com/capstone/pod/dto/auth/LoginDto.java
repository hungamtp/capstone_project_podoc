package com.capstone.pod.dto.auth;

import com.capstone.pod.constant.validation_message.ValidationMessage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginDto {

    @Email(message = ValidationMessage.EMAIL_VALID_MESSAGE_WHEN_LOGIN)
    private String email;

    @Size(min = 6, message = ValidationMessage.PASSWORD_SIZE_VALID_MESSAGE)
    private String password;
}
