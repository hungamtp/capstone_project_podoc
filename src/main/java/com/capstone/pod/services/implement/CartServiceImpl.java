package com.capstone.pod.services.implement;

import com.capstone.pod.constant.common.EntityName;
import com.capstone.pod.constant.common.ErrorMessage;
import com.capstone.pod.converter.CartDetailConverter;
import com.capstone.pod.dto.cartdetail.CartDetailDTO;
import com.capstone.pod.dto.cartdetail.CartNotEnough;
import com.capstone.pod.entities.*;
import com.capstone.pod.exceptions.CredentialNotFoundException;
import com.capstone.pod.repositories.*;
import com.capstone.pod.services.CartService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class CartServiceImpl implements CartService {
    private CartRepository cartRepository;
    private CredentialRepository credentialRepository;
    private CartDetailConverter cartDetailConverter;
    private CartDetailRepository cartDetailRepository;
    private SizeRepository sizeRepository;
    private ColorRepository colorRepository;
    private SizeColorRepository sizeColorRepository;
    private DesignedProductRepository designedProductRepository;
    private SizeColorByFactoryRepository sizeColorByFactoryRepository;

    public List<CartDetailDTO> getCard(String email) {
        Cart cart = getCartByEmail(email);

        if (cart.getCartDetails() == null) {
            return null;
        }

        return cartDetailConverter.entityToDtos(cart.getCartDetails());
    }

    public void deleteCartDetail(final Integer cartDetailId, String email) {
        //The deleteById in Spring Data JPA first does a findById which in your case, loads the associated entities eagerly.
        // Can not use fetch EAGER in ManyToOne To deleteById
        Cart cart = getCartByEmail(email);
        CartDetail cartDetail = cartDetailRepository.findById(cartDetailId)
            .orElseThrow(() -> new EntityNotFoundException(EntityName.CART_DETAIL + ErrorMessage.NOT_FOUND));

        if (cartDetail.getCart().getId() != cart.getId())
            throw new IllegalStateException(EntityName.CART + ErrorMessage.WRONG);

        cartDetailRepository.deleteById(cartDetailId);
    }

    public void updateCart(List<CartDetailDTO> dtos, String email) {
        Cart cart = getCartByEmail(email);
        cart.setCartDetails(cartDetailConverter.dtoToEntities(dtos, cart.getId()));
        cartRepository.save(cart);
    }

    public List<CartNotEnough> checkQuantityBeforeOrder(List<CartDetailDTO> cartDetails, String email) {
        Cart cart = getCartByEmail(email);
        List<CartNotEnough> productHaveNotEnoughQuantity = new ArrayList<>();
        for (var cartDetail : cartDetails) {
            // if checkSizeColorQuantity() return 0 if enough quantity to order
            Integer checkedQuantity = checkSizeColorQuantity(cartDetail.getSize(), cartDetail.getColor(), cartDetail.getDesignedProductId(), cartDetail.getQuantity());
            if (checkedQuantity != 0) {
                productHaveNotEnoughQuantity.add(new CartNotEnough(cart.getId(), checkedQuantity));
            }
        }
        return productHaveNotEnoughQuantity;
    }

    private Integer checkSizeColorQuantity(String sizeName, String colorName, int designedProductId, int quantity) {
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

}
