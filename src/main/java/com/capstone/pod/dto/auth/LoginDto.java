package com.capstone.pod.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginDto {

    @Email(message = "Wrong email address, please try again!")
    private String email;

    @Size(min = 6, message = "Password length must be greater than 6 characters")
    private String password;
}
