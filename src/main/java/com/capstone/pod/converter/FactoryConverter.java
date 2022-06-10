package com.capstone.pod.converter;

import com.capstone.pod.dto.factory.FactoryProductDetailDTO;
import com.capstone.pod.entities.Factory;
import org.springframework.stereotype.Component;

@Component
public class FactoryConverter {

    public static FactoryProductDetailDTO convertEntityToFactoryProductDetailDTO(Factory factory){
        return FactoryProductDetailDTO.builder()
            .id(factory.getId())
            .name(factory.getName())
            .build();
    }
}
