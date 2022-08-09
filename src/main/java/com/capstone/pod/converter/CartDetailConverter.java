package com.capstone.pod.converter;

import com.capstone.pod.constant.common.EntityName;
import com.capstone.pod.constant.common.ErrorMessage;
import com.capstone.pod.dto.cartdetail.CartDetailDto;
import com.capstone.pod.entities.*;
import com.capstone.pod.repositories.ColorRepository;
import com.capstone.pod.repositories.CredentialRepository;
import com.capstone.pod.repositories.UserRepository;
import org.apache.catalina.core.ApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CartDetailConverter {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CredentialRepository credentialRepository;

    @Autowired
    private ColorRepository colorRepository;


    public List<CartDetailDto> entityToDtos(List<CartDetail> cartDetails) {
        return cartDetails.stream().map((cartDetail -> entityToDto(cartDetail))).collect(Collectors.toList());
    }

    public List<CartDetail> dtoToEntities(List<CartDetailDto> dtos, String cartId) {
        return dtos.stream().map((dto -> dtoToEntity(dto, cartId))).collect(Collectors.toList());
    }

    public CartDetailDto entityToDto(CartDetail cartDetail) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentCredentialId = (String) authentication.getCredentials();
        Credential credential = credentialRepository.findById(currentCredentialId).orElseThrow(
            () -> new EntityNotFoundException(EntityName.CREDENTIAL + "_" + ErrorMessage.NOT_FOUND)
        );

        boolean publish = false;


        DesignedProduct designedProduct = cartDetail.getDesignedProduct();
        if (designedProduct.getProduct().isDeleted() || !designedProduct.getProduct().isPublic()) {
            publish = false;
        } else {
            if (designedProduct.getUser().getId().equals(credential.getUser().getId())) {
                publish = true;
            } else {
                publish = designedProduct.isPublish();
            }
        }
        Color color = colorRepository.findByName(cartDetail.getColor()).orElseThrow(
            () -> new EntityNotFoundException(EntityName.COLOR + "_" + ErrorMessage.NOT_FOUND)
        );
        var imagePreview = cartDetail.getDesignedProduct().getImagePreviews()
            .stream()
            .filter(image -> image.getPosition().equalsIgnoreCase("front"))
            .filter(image1 -> image1.getColor().equalsIgnoreCase(color.getImageColor()))
            .collect(Collectors.toList());
        return CartDetailDto.builder()
            .id(cartDetail.getId()).cartId(cartDetail.getCart().getId())
            .designedProductId(cartDetail.getDesignedProduct().getId())
            .designedProductName(cartDetail.getDesignedProduct().getName())
            .color(cartDetail.getColor())
            .size(cartDetail.getSize())
            .publish(publish)
            .designedImage(imagePreview.size() != 0 ? imagePreview.get(0).getImage() : "")
            .price(Double.valueOf(cartDetail.getDesignedProduct().getDesignedPrice() + cartDetail.getDesignedProduct().getPriceByFactory().getPrice()).floatValue())
            .quantity(cartDetail.getQuantity())
            .build();
    }

    private CartDetail dtoToEntity(CartDetailDto cartDetailDTO, String cartId) {
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
