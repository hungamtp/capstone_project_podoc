package com.capstone.pod.services.implement;

import com.capstone.pod.constant.common.EntityName;
import com.capstone.pod.constant.common.ErrorMessage;
import com.capstone.pod.converter.CartDetailConverter;
import com.capstone.pod.dto.cartdetail.CartDetailDTO;
import com.capstone.pod.entities.Cart;
import com.capstone.pod.entities.CartDetail;
import com.capstone.pod.entities.Credential;
import com.capstone.pod.entities.User;
import com.capstone.pod.exceptions.CredentialNotFoundException;
import com.capstone.pod.exceptions.UserNotFoundException;
import com.capstone.pod.repositories.CartDetailRepository;
import com.capstone.pod.repositories.CartRepository;
import com.capstone.pod.repositories.CredentialRepository;
import com.capstone.pod.repositories.UserRepository;
import com.capstone.pod.services.CartService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CartServiceImpl implements CartService {
    private CartRepository cartRepository;
    private CredentialRepository credentialRepository;
    private CartDetailConverter cartDetailConverter;

    public List<CartDetailDTO> getCard(String email) {
        Cart cart = getCartByEmail(email);

        if (cart.getCartDetails() == null) {
            return null;
        }

        return cartDetailConverter.entityToDtos(cart.getCartDetails());
    }

    public void deleteCartDetail(Integer cartDetailId, String email) {
        Cart cart = getCartByEmail(email);
        List<CartDetail> cartDetails = cart.getCartDetails();
        List<CartDetail> updatedCartDetails = cartDetails.stream().filter((cartDetail -> cartDetail.getId() != cartDetailId)).collect(Collectors.toList());
        cart.setCartDetails(updatedCartDetails);
        cartRepository.save(cart);
    }

    public void updateCart(List<CartDetailDTO> dtos, String email) {
        Cart cart = getCartByEmail(email);
        cart.setCartDetails(cartDetailConverter.dtoToEntities(dtos, cart.getId()));
        cartRepository.save(cart);
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
