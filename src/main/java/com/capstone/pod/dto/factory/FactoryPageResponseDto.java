package com.capstone.pod.dto.factory;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class FactoryPageResponseDto {
    private String email;
    private String address;
    private String phone;
    private String image;
    private String name;
    private String location;
    private boolean isCollaborating;
}
