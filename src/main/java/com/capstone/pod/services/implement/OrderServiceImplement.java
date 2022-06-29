package com.capstone.pod.services.implement;

import com.capstone.pod.constant.cart.CartErrorMessage;
import com.capstone.pod.constant.common.CommonMessage;
import com.capstone.pod.constant.common.EntityName;
import com.capstone.pod.constant.common.ErrorMessage;
import com.capstone.pod.constant.credential.CredentialErrorMessage;
import com.capstone.pod.constant.order.OrderState;
import com.capstone.pod.constant.product.ProductErrorMessage;
import com.capstone.pod.constant.sizecolor.SizeColorErrorMessage;
import com.capstone.pod.dto.order.ReturnOrderDto;
import com.capstone.pod.dto.order.ShippingInfoDto;
import com.capstone.pod.entities.*;
import com.capstone.pod.exceptions.*;
import com.capstone.pod.repositories.*;
import com.capstone.pod.services.OrdersService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImplement implements OrdersService {
    private final CartRepository cartRepository;
    private final CartDetailRepository cartDetailRepository;
    private final OrdersRepository ordersRepository;
    private final ShippingInfoRepository shippingInfoRepository;
    private final SizeColorByFactoryRepository sizeColorByFactoryRepository;
    private final SizeColorRepository sizeColorRepository;
    private final CredentialRepository credentialRepository;
    private final OrderStatusRepository orderStatusRepository;
    private final ModelMapper modelMapper;

    private Credential getCredential(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Integer currentCredentialId = (Integer)authentication.getCredentials();
        Credential credential = credentialRepository.findById(currentCredentialId).orElseThrow(() -> new CredentialNotFoundException(CredentialErrorMessage.CREDENTIAL_NOT_FOUND_EXCEPTION));
        return credential;
    }
    @Override
    @Transactional
    public ReturnOrderDto addOrder(int cartId, ShippingInfoDto shippingInfoDto) {
        Cart cart = cartRepository.findById(cartId).orElseThrow(() -> new CartNotFoundException(CartErrorMessage.CART_NOT_FOUND_ERROR));
        if(cart.getUser().getId() != getCredential().getUser().getId()){
            throw new PermissionException(CommonMessage.PERMISSION_EXCEPTION);
        }
        ShippingInfo shippingInfo = ShippingInfo.builder()
                .phoneNumber(shippingInfoDto.getPhone())
                .name(shippingInfoDto.getName())
                .shippingAddress(shippingInfoDto.getAddress())
                .emailAddress(shippingInfoDto.getEmail())
                .user(getCredential().getUser())
                .build();
        shippingInfoRepository.save(shippingInfo);
        List<CartDetail> cartDetailList = cart.getCartDetails();
        if(cartDetailList.isEmpty()) throw new CartNotFoundException(ErrorMessage.CART_EMPTY);
        Credential currentCredential = getCredential();
        double totalPrice = 0;
        String address  = currentCredential.getAddress();
        String phone = currentCredential.getPhone();
        String customerName = currentCredential.getUser().getLastName() + " " +currentCredential.getUser().getFirstName();
        Orders order = Orders.builder().address(address).customerName(customerName).phone(phone).user(currentCredential.getUser()).build();
        OrderStatus orderStatus = OrderStatus.builder().name(OrderState.PENDING).build();
        List<SizeColorByFactory> sizeColorByFactories = new ArrayList<>();
        List<OrderDetail> orderDetails = new ArrayList<>();
        for (int i = 0; i < cartDetailList.size(); i++) {
            if( cartDetailList.get(i).getDesignedProduct().getProduct().isDeleted() || !cartDetailList.get(i).getDesignedProduct().isPublish() ) {
                throw new ProductNotFoundException(ProductErrorMessage.PRODUCT_NOT_SUPPORT_FOR_ORDER);
            }
           OrderDetail orderDetail = OrderDetail.builder()
                   .orders(order)
                   .orderStatuses(Arrays.asList(orderStatus))
                   .quantity(cartDetailList.get(i).getQuantity())
                   .orderStatuses(Arrays.asList(orderStatus))
                   .color(cartDetailList.get(i).getColor())
                   .size(cartDetailList.get(i).getSize())
                   .factory(cartDetailList.get(i).getDesignedProduct().getPriceByFactory().getFactory())
                   .designedProduct(cartDetailList.get(i).getDesignedProduct()).build();
            orderStatus.setOrderDetail(orderDetail);
            int finalI = i;
            SizeColor sizeColor = sizeColorRepository
                   .findByColorNameAndSizeNameAndProductId(orderDetail.getColor(),orderDetail.getSize(),orderDetail.getDesignedProduct().getProduct().getId())
                   .orElseThrow(() -> new SizeNotFoundException(String.format(SizeColorErrorMessage.SIZE_AND_COLOR_NOT_EXIST_EXCEPTION,cartDetailList.get(finalI).getColor(),cartDetailList.get(finalI).getSize(),cartDetailList.get(finalI).getDesignedProduct().getProduct().getName())));

            SizeColorByFactory sizeColorByFactory = sizeColorByFactoryRepository.findByFactoryAndSizeColor(orderDetail.getDesignedProduct().getPriceByFactory().getFactory(),sizeColor)
                   .orElseThrow(() -> new SizeNotFoundException(SizeColorErrorMessage.SIZE_AND_COLOR_NOT_EXISTED_IN_FACTORY_EXCEPTION));
           if(sizeColorByFactory.getQuantity() < cartDetailList.get(i).getQuantity()){
               throw new QuantityNotEnoughException(ProductErrorMessage.QUANTITY_BY_FACTORY_NOT_ENOUGH);
           }
           sizeColorByFactory.setQuantity(sizeColorByFactory.getQuantity()-cartDetailList.get(i).getQuantity());
           sizeColorByFactories.add(sizeColorByFactory);
           orderDetails.add(orderDetail);
           totalPrice += cartDetailList.get(i).getQuantity()* ( cartDetailList.get(i).getDesignedProduct().getPriceByFactory().getPrice() + cartDetailList.get(i).getDesignedProduct().getDesignedPrice());
        }
        sizeColorByFactoryRepository.saveAll(sizeColorByFactories);
        order.setOrderDetails(orderDetails);
        order.setPrice(totalPrice);
        order.setAddress(shippingInfo.getEmailAddress());
        order.setPhone(shippingInfo.getPhoneNumber());
        order.setCustomerName(shippingInfo.getName());
        order.setPaid(false);
        order.setTransactionId("");
        ordersRepository.save(order);
        cartDetailRepository.deleteAllInBatch(cartDetailList);
        return ReturnOrderDto.builder().id(order.getId())
                .address(shippingInfo.getShippingAddress())
                .phone(shippingInfo.getPhoneNumber())
                .price(totalPrice)
                .transactionId(order.getTransactionId())
                .customerName(shippingInfoDto.getName())
                .build();
    }

    @Override
    public void setPaymentIdForOrder(int orderId, String paymentId) {
        Orders orders = ordersRepository.findById(orderId).orElseThrow(
            () -> new EntityNotFoundException(EntityName.ORDERS + ErrorMessage.NOT_FOUND)
        );

        if(orders.isPaid()){
            throw new IllegalStateException(EntityName.ORDERS + ErrorMessage.HAS_PAID);
        }

        orders.setTransactionId(paymentId);
        ordersRepository.save(orders);
    }

    @Override
    public void completeOrder(String paymentId) {
        Orders orders = ordersRepository.findByTransactionId(paymentId).orElseThrow(
            () -> new EntityNotFoundException(EntityName.ORDERS + ErrorMessage.NOT_FOUND)
        );

        orders.setPaid(true);
        ordersRepository.save(orders);
    }
}
