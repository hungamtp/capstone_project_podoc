package com.capstone.pod.dto.user;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
@Data
public class AddUserDto {
    @NotBlank(message = "First Name must be filled")
    private String firstName;
    @NotBlank(message = "Last Name must be filled")
    private String lastName;
    @NotBlank(message = "User Name must be filled")
    private String username;
    @NotBlank(message = "UserName must be filled")
    private String password;
    @NotBlank(message = "Address  must be filled")
    private String address;
    @NotBlank(message = "Phone must be filled")
    private String phone;
    @Email(message = "Email has to be correct")
    private String email;
    @NotBlank(message = "Role must be filled")
    private String roleName;
    private String avatar;
}
