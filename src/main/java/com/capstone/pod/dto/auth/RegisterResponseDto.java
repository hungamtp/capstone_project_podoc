package com.capstone.pod.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterResponseDto implements Serializable {
    private int userId;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String roleName;
    private String phone;
    private String avatar;
    private String address;
}
