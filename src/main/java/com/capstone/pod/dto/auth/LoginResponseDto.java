package com.capstone.pod.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginResponseDto {
    private String credentialId;
    private String email;
    private String roleName;
    private String token;
    private String phone;
    private String address;
    private String image;
}
