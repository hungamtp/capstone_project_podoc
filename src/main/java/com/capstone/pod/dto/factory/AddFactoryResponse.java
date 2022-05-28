package com.capstone.pod.dto.factory;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AddFactoryResponse {
    private int id;
    private String email;
    private String roleName;
    private String phone;
    private String address;
    private String image;
    private boolean isCollaborating;
}
