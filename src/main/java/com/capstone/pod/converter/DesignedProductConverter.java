package com.capstone.pod.converter;

import com.capstone.pod.dto.designedProduct.DesignedProductDto;
import com.capstone.pod.entities.DesignedProduct;
import com.capstone.pod.entities.OrderDetail;
import com.capstone.pod.entities.Rating;
import org.springframework.stereotype.Component;

@Component
public class DesignedProductConverter {

    public DesignedProductDto entityToDesignedProductDto(DesignedProduct designedProduct){
        return DesignedProductDto.builder()
            .id(designedProduct.getId())
            .name(designedProduct.getName())
            .rate(designedProduct.getRatings() == null ? 0:designedProduct.getRatings().stream().mapToDouble(Rating::getRatingStar).average().orElse(0))
            .designedPrice(designedProduct.getDesignedPrice())
            .image(designedProduct.getImagePreviews().stream().findFirst().get().getImage())
            .userId(designedProduct.getUser().getId())
            .username(designedProduct.getUser().getFirstName()+" " + designedProduct.getUser().getLastName())
            .soldCount(Integer.valueOf(designedProduct.getOrderDetails().stream().mapToInt(OrderDetail::getQuantity).sum()).longValue())
            .build();
    }
}
