package com.capstone.pod.dto.credential;

import lombok.Data;

@Data
public class CredentialDto {
    private String credentialId;
    private String email;
    private String roleName;
    private String token;
    private String phone;
    private String address;
    private String image;
}
