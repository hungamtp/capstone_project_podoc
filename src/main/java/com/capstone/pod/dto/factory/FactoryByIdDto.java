package com.capstone.pod.dto.factory;

import com.capstone.pod.dto.product.ProductDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Setter
@Getter
@Builder
public class FactoryByIdDto {
    private String id;
    private String email;
    private String name;
    private String location;
    private String phone;
    private String address;
    private String image;
    private boolean isCollaborating;
    private List<ProductDto> productDtoList;
}
