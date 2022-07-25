package com.capstone.pod.converter;

import com.capstone.pod.dto.cartdetail.CartDetailDto;
import com.capstone.pod.entities.Cart;
import com.capstone.pod.entities.CartDetail;
import com.capstone.pod.entities.DesignedProduct;
import com.capstone.pod.entities.ImagePreview;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CartDetailConverter {


    public List<CartDetailDto> entityToDtos(List<CartDetail> cartDetails) {
        return cartDetails.stream().map((cartDetail -> entityToDto(cartDetail))).collect(Collectors.toList());
    }

    public List<CartDetail> dtoToEntities(List<CartDetailDto> dtos , String cartId) {
        return dtos.stream().map((dto -> dtoToEntity(dto , cartId))).collect(Collectors.toList());
    }

    public CartDetailDto entityToDto(CartDetail cartDetail) {

        return CartDetailDto.builder()
            .id(cartDetail.getId()).cartId(cartDetail.getCart().getId())
            .designedProductId(cartDetail.getDesignedProduct().getId())
            .designedProductName(cartDetail.getDesignedProduct().getName())
            .color(cartDetail.getColor())
            .size(cartDetail.getSize())
            .publish(cartDetail.getDesignedProduct().isPublish())
            .designedImage(cartDetail.getDesignedProduct().getImagePreviews()
                .stream()
                .filter(imagePreview -> imagePreview.getPosition().equalsIgnoreCase("front"))
                .collect(Collectors.toList()).get(0).getImage())
            .price(Double.valueOf(cartDetail.getDesignedProduct().getDesignedPrice() + cartDetail.getDesignedProduct().getPriceByFactory().getPrice()).floatValue())
            .quantity(cartDetail.getQuantity())
            .build();
    }

    private CartDetail dtoToEntity(CartDetailDto cartDetailDTO , String cartId) {
        return CartDetail.builder()
            .id(cartDetailDTO.getId())
            .designedProduct(DesignedProduct.builder().id(cartDetailDTO.getDesignedProductId()).build())
            .size(cartDetailDTO.getSize())
            .color(cartDetailDTO.getColor())
            .quantity(cartDetailDTO.getQuantity())
            .cart(Cart.builder().id(cartId).build())
            .build();
    }
}
