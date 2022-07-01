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
    private String credentialId;
    private String email;
    private String roleName;
    private String phone;
    private String address;
    private String image;
}
