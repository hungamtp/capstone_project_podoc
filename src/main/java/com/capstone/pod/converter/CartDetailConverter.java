package com.capstone.pod.converter;

import com.capstone.pod.dto.cartdetail.CartDetailDTO;
import com.capstone.pod.entities.Cart;
import com.capstone.pod.entities.CartDetail;
import com.capstone.pod.entities.DesignedProduct;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CartDetailConverter {



    public List<CartDetailDTO> entityToDtos(List<CartDetail> cartDetails){
        return cartDetails.stream().map((cartDetail -> entityToDto(cartDetail))).collect(Collectors.toList());
    }

    public List<CartDetail> dtoToEntities(List<CartDetailDTO> dtos){
        return dtos.stream().map((dto -> dtoToEntity(dto))).collect(Collectors.toList());
    }

    private CartDetailDTO entityToDto(CartDetail cartDetail){
        return CartDetailDTO.builder()
            .id(cartDetail.getId())
            .designedProductId(cartDetail.getDesignedProduct().getId())
            .designedProductName(cartDetail.getDesignedProduct().getName())
            .color(cartDetail.getColor())
            .size(cartDetail.getSize())
            .quantity(cartDetail.getQuantity())
            .build();
    }

    private CartDetail dtoToEntity(CartDetailDTO cartDetailDTO){
        return CartDetail.builder()
            .id(cartDetailDTO.getId())
            .designedProduct(DesignedProduct.builder().id(cartDetailDTO.getDesignedProductId()).build())
            .size(cartDetailDTO.getSize())
            .color(cartDetailDTO.getColor())
            .quantity(cartDetailDTO.getQuantity())
            .build();
    }
}
