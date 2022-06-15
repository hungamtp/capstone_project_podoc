package com.capstone.pod.dto.factory;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FactoryPageResponseDto {
    private String email;
    private String address;
    private String phone;
    private String image;
    private String factoryName;
    private String fatoryLocation;
    private boolean factoryIsCollaborating;
}
