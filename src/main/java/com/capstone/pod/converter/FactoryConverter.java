package com.capstone.pod.converter;

import com.capstone.pod.dto.factory.FactoryProductDetailDto;
import com.capstone.pod.entities.Factory;
import org.springframework.stereotype.Component;

@Component
public class FactoryConverter {

    public static FactoryProductDetailDto convertEntityToFactoryProductDetailDTO(Factory factory){
        return FactoryProductDetailDto.builder()
            .id(factory.getId())
            .name(factory.getName())
            .build();
    }
}
