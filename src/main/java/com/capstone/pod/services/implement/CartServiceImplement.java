package com.capstone.pod.services.implement;

import com.capstone.pod.constant.common.EntityName;
import com.capstone.pod.constant.common.ErrorMessage;
import com.capstone.pod.constant.credential.CredentialErrorMessage;
import com.capstone.pod.converter.CartDetailConverter;
import com.capstone.pod.dto.cartdetail.AddToCartDto;
import com.capstone.pod.dto.cartdetail.CartDetailDto;
import com.capstone.pod.dto.cartdetail.CartNotEnoughDto;
import com.capstone.pod.entities.*;
import com.capstone.pod.exceptions.CredentialNotFoundException;
import com.capstone.pod.repositories.*;
import com.capstone.pod.services.CartService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CartServiceImplement implements CartService {
    private CartRepository cartRepository;
    private CredentialRepository credentialRepository;
    private CartDetailConverter cartDetailConverter;
    private CartDetailRepository cartDetailRepository;
    private SizeRepository sizeRepository;
    private ColorRepository colorRepository;
    private SizeColorRepository sizeColorRepository;
    private DesignedProductRepository designedProductRepository;
    private SizeColorByFactoryRepository sizeColorByFactoryRepository;

    public List<CartDetailDto> getCart(String email) {
        Cart cart = getCartByEmail(email);
        if (cart.getCartDetails() == null) {
            return null;
        }
        return cartDetailConverter.entityToDtos(cart.getCartDetails());
    }

    public String deleteCartDetail(final String cartDetailId, String email) {
        //The deleteById in Spring Data JPA first does a findById which in your case, loads the associated entities eagerly.
        // Can not use fetch EAGER in ManyToOne To deleteById
        Cart cart = getCartByEmail(email);
        CartDetail cartDetail = cartDetailRepository.findById(cartDetailId)
            .orElseThrow(() -> new EntityNotFoundException(EntityName.CART_DETAIL + ErrorMessage.NOT_FOUND));

        if (cartDetail.getCart().getId() != cart.getId())
            throw new IllegalStateException(EntityName.CART + ErrorMessage.WRONG);

        cartDetailRepository.deleteById(cartDetailId);
        return cartDetailId;
    }

    public void updateCart(List<CartDetailDto> dtos, String email) {
        Cart cart = getCartByEmail(email);
        cart.setCartDetails(cartDetailConverter.dtoToEntities(dtos, cart.getId()));
        cartRepository.save(cart);
    }

    public List<CartNotEnoughDto> checkQuantityBeforeOrder(List<CartDetailDto> cartDetails, String email) {
        Cart cart = getCartByEmail(email);
        List<CartNotEnoughDto> productHaveNotEnoughQuantity = new ArrayList<>();
        for (var cartDetail : cartDetails) {
            // if checkSizeColorQuantity() return 0 if enough quantity to order
            Integer checkedQuantity = checkSizeColorQuantity(cartDetail.getSize(), cartDetail.getColor(), cartDetail.getDesignedProductId(), cartDetail.getQuantity());
            if (checkedQuantity != 0) {
                productHaveNotEnoughQuantity.add(new CartNotEnoughDto(cart.getId(), checkedQuantity));
            }
        }
        return productHaveNotEnoughQuantity;
    }

    private List<CartNotEnoughDto> checkQuantity(List<CartDetailDto> cartDetails, Cart cart) {
        List<CartNotEnoughDto> productHaveNotEnoughQuantity = new ArrayList<>();
        for (var cartDetail : cartDetails) {
            // if checkSizeColorQuantity() return 0 if enough quantity to order
            Integer checkedQuantity = checkSizeColorQuantity(cartDetail.getSize(), cartDetail.getColor(), cartDetail.getDesignedProductId(), cartDetail.getQuantity());
            if (checkedQuantity != 0) {
                productHaveNotEnoughQuantity.add(new CartNotEnoughDto(cart.getId(), checkedQuantity));
            }
        }
        return productHaveNotEnoughQuantity;
    }


    public CartDetailDto addToCart(AddToCartDto addToCartDto, String email) {
        Cart cart = getCartByEmail(email);
        DesignedProduct designedProduct = designedProductRepository.findById(addToCartDto.getDesignId())
            .orElseThrow(() -> new EntityNotFoundException(EntityName.DESIGNED_PRODUCT + ErrorMessage.NOT_FOUND)
            );
        Credential credential = getCredential();

        if(!designedProduct.getUser().getId().equals(credential.getUser().getId())){
            if (!designedProduct.isPublish())
                throw new EntityNotFoundException(EntityName.DESIGNED_PRODUCT + ErrorMessage.NOT_FOUND);
        }



        Optional<CartDetail> cartDetail = cartDetailRepository
            .findByDesignedProductAndColorAndSize(designedProduct, addToCartDto.getColor(), addToCartDto.getSize());

        if (cartDetail.isPresent()) {
            CartDetail savedCartDetail = cartDetail.get();
            savedCartDetail.setQuantity(savedCartDetail.getQuantity() + addToCartDto.getQuantity());
            cartDetailRepository.save(savedCartDetail);
           return CartDetailDto
               .builder()
               .id(cartDetail.get().getId())
               .cartId(cartDetail.get().getCart().getId())
               .designedProductId(cartDetail.get().getDesignedProduct().getId())
               .designedProductName(cartDetail.get().getDesignedProduct().getName())
               .color(cartDetail.get().getColor())
               .size(cartDetail.get().getSize())
               .publish(true)
               .designedImage(cartDetail.get().getDesignedProduct().getImagePreviews().stream().map(ImagePreview::getImage).collect(Collectors.toList()).get(0))
               .price(Double.valueOf(cartDetail.get().getDesignedProduct().getDesignedPrice() + cartDetail.get().getDesignedProduct().getPriceByFactory().getPrice()).floatValue())
               .quantity(savedCartDetail.getQuantity())
               .build();
        } else {
        CartDetail savedCartDetail = cartDetailRepository.save(
                CartDetail.builder()
                    .cart(cart)
                    .size(addToCartDto.getSize())
                    .color(addToCartDto.getColor())
                    .quantity(addToCartDto.getQuantity())
                    .designedProduct(designedProduct)
                    .build());
            return CartDetailDto.builder()
                .id(savedCartDetail.getId())
                .cartId(savedCartDetail.getCart().getId())
                .designedProductId(savedCartDetail.getDesignedProduct().getId())
                .designedProductName(savedCartDetail.getDesignedProduct().getName())
                .color(savedCartDetail.getColor())
                .size(savedCartDetail.getSize())
                .publish(true)
                .designedImage(savedCartDetail.getDesignedProduct().getImagePreviews().stream().map(ImagePreview::getImage).collect(Collectors.toList()).get(0))
                .price(Double.valueOf(savedCartDetail.getDesignedProduct().getDesignedPrice() + savedCartDetail.getDesignedProduct().getPriceByFactory().getPrice()).floatValue())
                .quantity(savedCartDetail.getQuantity())
                .build();
        }
    }

    private Integer checkSizeColorQuantity(String sizeName, String colorName, String designedProductId, int quantity) {
        Size size = sizeRepository.findByName(sizeName).orElseThrow(
            () -> new EntityNotFoundException(EntityName.SIZE + ErrorMessage.NOT_FOUND)
        );
        Color color = colorRepository.findByName(colorName).orElseThrow(
            () -> new EntityNotFoundException(EntityName.COLOR + ErrorMessage.NOT_FOUND)
        );
        DesignedProduct designedProduct = designedProductRepository.findById(designedProductId).orElseThrow(
            () -> new EntityNotFoundException(EntityName.DESIGNED_PRODUCT + ErrorMessage.NOT_FOUND)
        );
        SizeColor sizeColor = sizeColorRepository.findSizeColorByColorAndSizeAndProduct(color, size, designedProduct.getProduct()).orElseThrow(
            () -> new EntityNotFoundException(EntityName.SIZE_COLOR + ErrorMessage.NOT_FOUND)
        );

        SizeColorByFactory sizeColorByFactory = sizeColorByFactoryRepository
            .findByFactoryAndSizeColor(designedProduct.getPriceByFactory().getFactory(), sizeColor)
            .orElseThrow(() -> new EntityNotFoundException(EntityName.SIZE_COLOR_FACTORY + ErrorMessage.NOT_FOUND));

        return sizeColorByFactory.getQuantity() < quantity ? sizeColorByFactory.getQuantity() : 0;
    }

    private Cart getCartByEmail(String email) {
        Credential credential = credentialRepository.findCredentialByEmail(email).orElseThrow(
            () -> new CredentialNotFoundException(EntityName.CREDENTIAL + ErrorMessage.NOT_FOUND)
        );

        User user = credential.getUser();

        Cart cart = cartRepository.findCartByUser(credential.getUser());

        if (cart == null) {
            cart = new Cart();
            cart.setUser(user);
            return cartRepository.save(cart);
        }

        return cart;
    }

    private Credential getCredential() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentCredentialId = (String) authentication.getCredentials();
        Credential credential = credentialRepository.findById(currentCredentialId.toString()).orElseThrow(() -> new CredentialNotFoundException(CredentialErrorMessage.CREDENTIAL_NOT_FOUND_EXCEPTION));
        return credential;
    }

}
