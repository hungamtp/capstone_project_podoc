package com.capstone.pod.dto.product;

import com.capstone.pod.dto.factory.FactoryForProductDetailsDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PriceByFactoryDto {
    private double price;
    private FactoryForProductDetailsDto factory;
}
