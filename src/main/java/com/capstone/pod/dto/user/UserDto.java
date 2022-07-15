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
    private String id;
    private String userFirstName;
    private String userLastName;
    private String name;
    private String email;
    private String roleName;
    private String phone;
    private String address;
    private String image;
    private String userStatus;
    private boolean isMailVerified;
}
