package com.capstone.pod.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserDto {
    private int id;
    private String email;
    private String phone;
    private String address;
    private String image;
    private String roleName;
    private String userStatus;
}
