package com.capstone.pod.services.implement;

import com.capstone.pod.constant.cart.CartErrorMessage;
import com.capstone.pod.constant.credential.CredentialErrorMessage;
import com.capstone.pod.constant.order.OrderErrorMessage;
import com.capstone.pod.constant.order.OrderState;
import com.capstone.pod.constant.product.ProductErrorMessage;
import com.capstone.pod.entities.*;
import com.capstone.pod.exceptions.CartNotFoundException;
import com.capstone.pod.exceptions.CredentialNotFoundException;
import com.capstone.pod.exceptions.OrderStatusNotFoundException;
import com.capstone.pod.exceptions.QuantityNotEnoughException;
import com.capstone.pod.repositories.*;
import com.capstone.pod.services.OrdersService;
import lombok.RequiredArgsConstructor;
import org.hibernate.criterion.Order;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderServiceImplement implements OrdersService {
    private final CartRepository cartRepository;
    private final OrdersRepository ordersRepository;
    private final SizeColorByFactoryRepository sizeColorByFactoryRepository;
    private final SizeColorRepository sizeColorRepository;
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
    @Transactional
    public void addOrder(int cartId) {
        Cart cart = cartRepository.findById(cartId).orElseThrow(() -> new CartNotFoundException(CartErrorMessage.CART_NOT_FOUND_ERROR));
        List<CartDetail> cartDetailList = cart.getCartDetails();
        Credential currentCredential = getCredential();
        double totalPrice = 0;
        String address  = currentCredential.getAddress();
        String phone = currentCredential.getPhone();
        String customerName = currentCredential.getUser().getLastName() + " " +currentCredential.getUser().getFirstName();
        OrderStatus orderStatus = orderStatusRepository.findByName(OrderState.PENDING).orElseThrow(() -> new OrderStatusNotFoundException(OrderErrorMessage.ORDER_NOT_FOUND_EXCEPTION));
        Orders order = Orders.builder().address(address).customerName(customerName).phone(phone).user(currentCredential.getUser()).build();
        List<SizeColorByFactory> sizeColorByFactories = new ArrayList<>();
        List<OrderDetail> orderDetails = new ArrayList<>();
        for (int i = 0; i < cartDetailList.size(); i++) {
           OrderDetail orderDetail = OrderDetail.builder()
                   .quantity(cartDetailList.get(i).getQuantity())
                   .orderStatuses(Arrays.asList(orderStatus))
                   .color(cartDetailList.get(i).getColor())
                   .size(cartDetailList.get(i).getSize())
                   .factory(cartDetailList.get(i).getDesignedProduct().getPriceByFactory().getFactory())
                   .factory(cartDetailList.get(i).getDesignedProduct().getPriceByFactory().getFactory())
                   .designedProduct(cartDetailList.get(i).getDesignedProduct()).build();
           Optional<SizeColor> sizeColor = sizeColorRepository.findByColorImageColorAndSizeNameAndProductId(cartDetailList.get(i).getColor(),cartDetailList.get(i).getSize(),cartDetailList.get(i).getDesignedProduct().getProduct().getId());
           Optional<SizeColorByFactory> sizeColorByFactory = sizeColorByFactoryRepository.findByFactoryAndSizeColor(cartDetailList.get(i).getDesignedProduct().getPriceByFactory().getFactory(),sizeColor.get());
           if(sizeColorByFactory.get().getQuantity() < cartDetailList.get(i).getQuantity()){
               throw new QuantityNotEnoughException(ProductErrorMessage.QUANTITY_BY_FACTORY_NOT_ENOUGH);
           }
           sizeColorByFactory.get().setQuantity(sizeColorByFactory.get().getQuantity()-cartDetailList.get(i).getQuantity());
           sizeColorByFactories.add(sizeColorByFactory.get());
           orderDetails.add(orderDetail);
           totalPrice += cartDetailList.get(i).getQuantity()* ( cartDetailList.get(i).getDesignedProduct().getPriceByFactory().getPrice() + cartDetailList.get(i).getDesignedProduct().getDesignedPrice());
        }
        sizeColorByFactoryRepository.saveAll(sizeColorByFactories);
        order.setOrderDetails(orderDetails);
        order.setPrice(totalPrice);
        order.setPaid(false);
        ordersRepository.save(order);
    }
}
