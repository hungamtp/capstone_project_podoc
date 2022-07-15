package com.capstone.pod.services.implement;

import com.capstone.pod.constant.cart.CartErrorMessage;
import com.capstone.pod.constant.common.CommonMessage;
import com.capstone.pod.constant.common.EntityName;
import com.capstone.pod.constant.common.ErrorMessage;
import com.capstone.pod.constant.credential.CredentialErrorMessage;
import com.capstone.pod.constant.order.OrderState;
import com.capstone.pod.constant.product.ProductErrorMessage;
import com.capstone.pod.constant.sizecolor.SizeColorErrorMessage;
import com.capstone.pod.constant.validation_message.ValidationMessage;
import com.capstone.pod.dto.common.PageDTO;
import com.capstone.pod.dto.order.AllOrderDto;
import com.capstone.pod.converter.OrderDetailConverter;
import com.capstone.pod.dto.order.MyOrderDetailDto;
import com.capstone.pod.dto.order.ReturnOrderDto;
import com.capstone.pod.dto.order.ShippingInfoDto;
import com.capstone.pod.entities.*;
import com.capstone.pod.enums.PaymentMethod;
import com.capstone.pod.exceptions.*;
import com.capstone.pod.momo.config.Environment;
import com.capstone.pod.momo.enums.RequestType;
import com.capstone.pod.momo.models.PaymentResponse;
import com.capstone.pod.momo.processor.CreateOrderMoMo;
import com.capstone.pod.repositories.*;
import com.capstone.pod.services.OrdersService;
import com.capstone.pod.zalo.ZaloService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static com.capstone.pod.zalo.ZaloService.getCurrentTimeString;

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
    private final OrderDetailRepository orderDetailRepository;
    private final ModelMapper modelMapper;
    private final ZaloService zaloService;
    private final OrderDetailConverter orderDetailConverter;

    private Credential getCredential() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentCredentialId = (String) authentication.getCredentials();
        Credential credential = credentialRepository.findById(currentCredentialId.toString()).orElseThrow(() -> new CredentialNotFoundException(CredentialErrorMessage.CREDENTIAL_NOT_FOUND_EXCEPTION));
        return credential;
    }

    @Override
    @Transactional
    public PaymentResponse addOrder(ShippingInfoDto shippingInfoDto, int paymentMethod) throws Exception {
        Cart cart = cartRepository.findCartByUser(getCredential().getUser());
        if (cart == null) throw new CartNotFoundException(CartErrorMessage.CART_NOT_FOUND_ERROR);
        if (cart.getUser().getId() != getCredential().getUser().getId()) {
            throw new PermissionException(CommonMessage.PERMISSION_EXCEPTION);
        }
        ShippingInfo shippingInfo = ShippingInfo.builder()
            .phoneNumber(shippingInfoDto.getPhone())
            .name(shippingInfoDto.getName())
            .shippingAddress(shippingInfoDto.getAddress())
            .emailAddress(shippingInfoDto.getEmail())
            .user(getCredential().getUser())
            .build();
        if (shippingInfoDto.isShouldSave()) {
            shippingInfoRepository.save(shippingInfo);
        }
        List<CartDetail> cartDetailList = cart.getCartDetails();
        if (cartDetailList.isEmpty()) throw new CartNotFoundException(ErrorMessage.CART_EMPTY);
        Credential currentCredential = getCredential();
        double totalPrice = 0;
        String address = currentCredential.getAddress();
        String phone = currentCredential.getPhone();
        String customerName = currentCredential.getUser().getLastName() + " " + currentCredential.getUser().getFirstName();
        Orders order = Orders.builder().address(address).customerName(customerName).phone(phone).user(currentCredential.getUser()).build();
        OrderStatus orderStatus = OrderStatus.builder().name(OrderState.PENDING).build();
        List<SizeColorByFactory> sizeColorByFactories = new ArrayList<>();
        List<OrderDetail> orderDetails = new ArrayList<>();
        for (int i = 0; i < cartDetailList.size(); i++) {
            if (cartDetailList.get(i).getDesignedProduct().getProduct().isDeleted()) {
                throw new ProductNotFoundException(ProductErrorMessage.PRODUCT_NOT_SUPPORT_FOR_ORDER);
            }
            if (!cartDetailList.get(i).getDesignedProduct().isPublish()) {
                if (!cartDetailList.get(i).getDesignedProduct().getUser().getId().equals(currentCredential.getUser().getId())) {
                    throw new ProductNotFoundException(ProductErrorMessage.PRODUCT_NOT_SUPPORT_FOR_ORDER);
                }
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
                .findByColorNameAndSizeNameAndProductId(orderDetail.getColor(), orderDetail.getSize(), orderDetail.getDesignedProduct().getProduct().getId())
                .orElseThrow(() -> new SizeNotFoundException(String.format(SizeColorErrorMessage.SIZE_AND_COLOR_NOT_EXIST_EXCEPTION, cartDetailList.get(finalI).getColor(), cartDetailList.get(finalI).getSize(), cartDetailList.get(finalI).getDesignedProduct().getProduct().getName())));

            SizeColorByFactory sizeColorByFactory = sizeColorByFactoryRepository.findByFactoryAndSizeColor(orderDetail.getDesignedProduct().getPriceByFactory().getFactory(), sizeColor)
                .orElseThrow(() -> new SizeNotFoundException(SizeColorErrorMessage.SIZE_AND_COLOR_NOT_EXISTED_IN_FACTORY_EXCEPTION));
            if (sizeColorByFactory.getQuantity() < cartDetailList.get(i).getQuantity()) {
                throw new QuantityNotEnoughException(ProductErrorMessage.QUANTITY_BY_FACTORY_NOT_ENOUGH);
            }
            sizeColorByFactory.setQuantity(sizeColorByFactory.getQuantity() - cartDetailList.get(i).getQuantity());
            sizeColorByFactories.add(sizeColorByFactory);
            orderDetails.add(orderDetail);
            totalPrice += cartDetailList.get(i).getQuantity() * (cartDetailList.get(i).getDesignedProduct().getPriceByFactory().getPrice() + cartDetailList.get(i).getDesignedProduct().getDesignedPrice());
        }
        if (totalPrice < 1000 || totalPrice > 50000000)
            throw new PermissionException(ValidationMessage.PRICE_TOTAL_SHOULD_VALID);

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

        // create payment info
        PaymentResponse paymentResponse = null;
        String orderInfo = String.format("Total : %f  , Phone : %s", order.getPrice(), order.getPhone());
        String requestId = String.valueOf(System.currentTimeMillis());
        String orderId = String.valueOf(System.currentTimeMillis());
        Double amount = order.getPrice();
        Environment environment = Environment.selectEnv("dev");
        String returnURL = environment.getMomoEndpoint().getRedirectUrl();
        String notifyURL = environment.getMomoEndpoint().getNotiUrl();
        Random rand = new Random();
        String transactionId = getCurrentTimeString("yyMMdd") + "_" + rand.nextInt(1000000);
        if (PaymentMethod.MOMO.ordinal() == paymentMethod) {
            paymentResponse = CreateOrderMoMo.process(environment, orderId, requestId, Long.valueOf(amount.longValue()).toString(), orderInfo, returnURL, notifyURL, "", RequestType.CAPTURE_WALLET, Boolean.TRUE);
        } else if (PaymentMethod.ZALO_PAY.ordinal() == paymentMethod) {
            paymentResponse = zaloService.createZaloPayOrder(amount.longValue(), orderInfo, transactionId);
        }

        if (paymentResponse == null) {
            if (paymentMethod == PaymentMethod.MOMO.ordinal()) {
                throw new IllegalStateException(PaymentMethod.MOMO + "_API_ERROR");
            }
            if (paymentMethod == PaymentMethod.ZALO_PAY.ordinal()) {
                throw new IllegalStateException(PaymentMethod.ZALO_PAY + "_API_ERROR");
            }
        } else {
            if (paymentMethod == PaymentMethod.MOMO.ordinal()) {
                setPaymentIdForOrder(order.getId(), paymentResponse.getOrderId());
            }
            if (paymentMethod == PaymentMethod.ZALO_PAY.ordinal()) {
                setPaymentIdForOrder(order.getId(), transactionId);
            }

        }

        return paymentResponse;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    void setPaymentIdForOrder(String orderId, String paymentId) {
        Orders orders = ordersRepository.findById(orderId).orElseThrow(
            () -> new EntityNotFoundException(EntityName.ORDERS + ErrorMessage.NOT_FOUND)
        );

        if (orders.isPaid()) {
            throw new IllegalStateException(EntityName.ORDERS + ErrorMessage.HAS_PAID);
        }

        orders.setTransactionId(paymentId);
        ordersRepository.updatePaymentId(orderId, paymentId);
    }

    @Override
    public void completeOrder(String paymentId) {
        Orders orders = ordersRepository.findByTransactionId(paymentId).orElseThrow(
            () -> new EntityNotFoundException(EntityName.ORDERS + ErrorMessage.NOT_FOUND)
        );
        if (orders.isPaid()) {
            throw new IllegalStateException(EntityName.ORDERS + ErrorMessage.HAS_PAID);
        }

        orders.setPaid(true);
        ordersRepository.save(orders);
    }

    @Override
    public List<ShippingInfoDto> getMyShippingInfo() {
        List<ShippingInfo> shippingInfoList = shippingInfoRepository.findAllByUserId(getCredential().getUser().getId());
        List<ShippingInfoDto> shippingInfoDtos = shippingInfoList.stream().map(shippingInfo -> ShippingInfoDto.builder()
            .id(shippingInfo.getId())
            .address(shippingInfo.getShippingAddress())
            .name(shippingInfo.getName())
            .email(shippingInfo.getEmailAddress())
            .phone(shippingInfo.getPhoneNumber())
            .build()).collect(Collectors.toList());
        return shippingInfoDtos;
    }

    @Override
    public PageDTO getAllOrderIsNotPaid(String email, Pageable pageable) {
        Credential credential = credentialRepository.findCredentialByEmail(email).orElseThrow(
            () -> new CredentialNotFoundException(EntityName.CREDENTIAL + ErrorMessage.NOT_FOUND)
        );
        User user = credential.getUser();
        Page<Orders> ordersPage = ordersRepository.findAllByIsPaidIsFalseAndUser(pageable, user);
        PageDTO pageDTO = new PageDTO();
        pageDTO.setPage(pageDTO.getPage());
        pageDTO.setElements(pageDTO.getElements());
        List<AllOrderDto> orderDtos = ordersPage.stream().map(orders -> {
            List<OrderDetail> orderDetails = orders.getOrderDetails();
            return AllOrderDto.builder()
                .orderId(orders.getId())
                .countItem(orderDetails.size())
                .isPaid(orders.isPaid())
                .totalBill(orders.getPrice())
                .createdDate(orders.getCreateDate())
                .build();
        }).collect(Collectors.toList());
        pageDTO.setData(orderDtos);
        return pageDTO;
    }

    @Override
    public PaymentResponse payOrder(int paymentMethod ,String orderID) throws Exception {
        Orders orders = ordersRepository.findById(orderID).orElseThrow(
            () -> new EntityNotFoundException(EntityName.ORDERS + ErrorMessage.NOT_FOUND)
        );
        if(orders.isPaid()){
            throw new IllegalStateException(EntityName.ORDERS + ErrorMessage.HAS_PAID);
        }
        PaymentResponse paymentResponse = null;
        String orderInfo = String.format("Total : %f  , Phone : %s", orders.getPrice(), orders.getPhone());
        String requestId = String.valueOf(System.currentTimeMillis());
        String orderId = String.valueOf(System.currentTimeMillis());
        Double amount = orders.getPrice();
        Environment environment = Environment.selectEnv("dev");
        String returnURL = environment.getMomoEndpoint().getRedirectUrl();
        String notifyURL = environment.getMomoEndpoint().getNotiUrl();

        String transactionId = null;
        if (PaymentMethod.MOMO.ordinal() == paymentMethod) {
            paymentResponse = CreateOrderMoMo.process(environment, orderId, requestId, Long.valueOf(amount.longValue()).toString(), orderInfo, returnURL, notifyURL, "", RequestType.CAPTURE_WALLET, Boolean.TRUE);
        } else if (PaymentMethod.ZALO_PAY.ordinal() == paymentMethod) {
            Random rand = new Random();
            transactionId = getCurrentTimeString("yyMMdd") + "_" + rand.nextInt(1000000);
            paymentResponse = zaloService.createZaloPayOrder(amount.longValue(), orderInfo, transactionId);
        }

        if (paymentResponse == null) {
            if (paymentMethod == PaymentMethod.MOMO.ordinal()) {
                throw new IllegalStateException(PaymentMethod.MOMO + "_API_ERROR");
            }
            if (paymentMethod == PaymentMethod.ZALO_PAY.ordinal()) {
                throw new IllegalStateException(PaymentMethod.ZALO_PAY + "_API_ERROR");
            }
        } else {
            if (paymentMethod == PaymentMethod.MOMO.ordinal()) {
                setPaymentIdForOrder(orders.getId(), paymentResponse.getOrderId());
            }
            if (paymentMethod == PaymentMethod.ZALO_PAY.ordinal()) {
                setPaymentIdForOrder(orders.getId(), transactionId);
            }

        }

        return paymentResponse;
    }


    public List<MyOrderDetailDto> getAllMyOrderDetail(int page, int size) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentCredentialId = (String) authentication.getCredentials();
        Credential credential = credentialRepository.findById(currentCredentialId)
            .orElseThrow(() -> new CredentialNotFoundException(CredentialErrorMessage.CREDENTIAL_NOT_FOUND_EXCEPTION));

        return orderDetailRepository.findAllOrderDetail(page, size, credential.getUser().getId()).stream().map(orderDetail -> orderDetailConverter.entityToMyOrderDetailDto(orderDetail)).collect(Collectors.toList());
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
