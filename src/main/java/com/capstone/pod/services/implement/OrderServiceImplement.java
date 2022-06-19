package com.capstone.pod.services.implement;

import com.capstone.pod.constant.cart.CartErrorMessage;
import com.capstone.pod.constant.credential.CredentialErrorMessage;
import com.capstone.pod.constant.order.OrderErrorMessage;
import com.capstone.pod.constant.order.OrderState;
import com.capstone.pod.entities.*;
import com.capstone.pod.exceptions.CartNotFoundException;
import com.capstone.pod.exceptions.CredentialNotFoundException;
import com.capstone.pod.exceptions.OrderStatusNotFoundException;
import com.capstone.pod.repositories.CartDetailRepository;
import com.capstone.pod.repositories.CartRepository;
import com.capstone.pod.repositories.CredentialRepository;
import com.capstone.pod.repositories.OrderStatusRepository;
import com.capstone.pod.services.OrdersService;
import lombok.RequiredArgsConstructor;
import org.hibernate.criterion.Order;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImplement implements OrdersService {
    private final CartRepository cartRepository;
    private final CartDetailRepository cartDetailRepository;
    private final CredentialRepository credentialRepository;
    private final OrderStatusRepository orderStatusRepository;

    private Credential getCredential(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Integer currentCredentialId = (Integer)authentication.getCredentials();
        Credential credential = credentialRepository.findById(currentCredentialId).orElseThrow(() -> new CredentialNotFoundException(CredentialErrorMessage.CREDENTIAL_NOT_FOUND_EXCEPTION));
        return credential;
    }
    @Override
    public void addOrder(int cartId) {
        Cart cart = cartRepository.findById(cartId).orElseThrow(() -> new CartNotFoundException(CartErrorMessage.CART_NOT_FOUND_ERROR));
        List<CartDetail> cartDetailList = cart.getCartDetails();
        Credential currentCredential = getCredential();
        double totalPrice = 0;
        String address  = currentCredential.getAddress();
        String phone = currentCredential.getPhone();
        String customerName = currentCredential.getUser().getLastName() + " " +currentCredential.getUser().getFirstName();
        OrderStatus orderStatus = orderStatusRepository.findByName(OrderState.PENDING).orElseThrow(() -> new OrderStatusNotFoundException(OrderErrorMessage.ORDER_NOT_FOUND_EXCEPTION));

        Orders order = Orders.builder().orderStatus(orderStatus).address(address).customerName(customerName).phone(phone).user(currentCredential.getUser()).build();
        for (int i = 0; i < cartDetailList.size(); i++) {

        }
    }
}
