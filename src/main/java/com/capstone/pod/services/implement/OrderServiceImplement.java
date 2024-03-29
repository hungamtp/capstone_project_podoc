package com.capstone.pod.services.implement;

import com.capstone.pod.constant.cart.CartErrorMessage;
import com.capstone.pod.constant.common.CommonMessage;
import com.capstone.pod.constant.common.EntityName;
import com.capstone.pod.constant.common.ErrorMessage;
import com.capstone.pod.constant.credential.CredentialErrorMessage;
import com.capstone.pod.constant.order.OrderErrorMessage;
import com.capstone.pod.constant.order.OrderState;
import com.capstone.pod.constant.product.ProductErrorMessage;
import com.capstone.pod.constant.sizecolor.SizeColorErrorMessage;
import com.capstone.pod.constant.validation_message.ValidationMessage;
import com.capstone.pod.converter.OrderDetailConverter;
import com.capstone.pod.dto.common.PageDTO;
import com.capstone.pod.dto.dashboard.AdminDashboard;
import com.capstone.pod.dto.dashboard.CategorySoldCountProjection;
import com.capstone.pod.dto.dashboard.DesignerDashboard;
import com.capstone.pod.dto.dashboard.FactoryDashboard;
import com.capstone.pod.dto.order.*;
import com.capstone.pod.entities.*;
import com.capstone.pod.enums.PaymentMethod;
import com.capstone.pod.exceptions.*;
import com.capstone.pod.momo.config.Environment;
import com.capstone.pod.momo.enums.RequestType;
import com.capstone.pod.momo.models.PaymentResponse;
import com.capstone.pod.momo.processor.CreateOrderMoMo;
import com.capstone.pod.repositories.*;
import com.capstone.pod.repositories.impl.projection.AllOrderDetail;
import com.capstone.pod.services.OrdersService;
import com.capstone.pod.zalo.ZaloService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.capstone.pod.zalo.ZaloService.getCurrentTimeString;

@Service
@RequiredArgsConstructor
public class OrderServiceImplement implements OrdersService {
    private final CartRepository cartRepository;
    private final CartDetailRepository cartDetailRepository;
    private final OrdersRepository ordersRepository;
    private final PrintingInfoRepository printingInfoRepository;

    private final ShippingInfoRepository shippingInfoRepository;
    private final SizeColorByFactoryRepository sizeColorByFactoryRepository;
    private final SizeColorRepository sizeColorRepository;
    private final CredentialRepository credentialRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final ZaloService zaloService;
    private final OrderDetailConverter orderDetailConverter;
    private final DesignedProductRepository designedProductRepository;
    private final OrderStatusRepository orderStatusRepository;

    private final EntityManager entityManager;

    private Credential getCredential() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentCredentialId = (String) authentication.getCredentials();
        Credential credential = credentialRepository.findById(currentCredentialId).orElseThrow(() -> new CredentialNotFoundException(CredentialErrorMessage.CREDENTIAL_NOT_FOUND_EXCEPTION));
        return credential;
    }

    private PrintingInfo saveDesignForPrinting(CartDetail detail, OrderDetail orderDetail) {
        PrintingInfo printingInfo = PrintingInfo.builder().build();
            printingInfo.setOrderDetail(orderDetail);
            List<PrintingImagePreview> printingImagePreviews = new ArrayList<>();
            for (int j = 0; j < detail.getDesignedProduct().getImagePreviews().size(); j++) {
                ImagePreview imagePreview = detail.getDesignedProduct().getImagePreviews().get(j);
                PrintingImagePreview printingImagePreview = PrintingImagePreview.builder().image(imagePreview.getImage()).color(imagePreview.getColor()).position(imagePreview.getPosition()).printingInfo(printingInfo).build();
                printingImagePreviews.add(printingImagePreview);
            }
            List<PrintingBluePrint> printingBluePrints = new ArrayList<>();
            for (int j = 0; j < detail.getDesignedProduct().getBluePrints().size(); j++) {
                Placeholder placeholder = detail.getDesignedProduct().getBluePrints().get(j).getPlaceholder();
                List<PrintingDesignInfo> printingDesignInfos = new ArrayList<>();
                PrintingBluePrint printingBluePrint = PrintingBluePrint.builder().frameImage(detail.getDesignedProduct().getBluePrints().get(j).getFrameImage()).position(detail.getDesignedProduct().getBluePrints().get(j).getPosition()).printingInfo(printingInfo).printingDesignInfos(printingDesignInfos).build();
                PrintingPlaceholder printingPlaceholder = PrintingPlaceholder.builder().printingBluePrint(printingBluePrint).top(placeholder.getTop()).width(placeholder.getWidth()).height(placeholder.getHeight()).widthRate(placeholder.getWidthRate()).heightRate(placeholder.getHeightRate()).build();
                printingBluePrint.setPrintingPlaceholder(printingPlaceholder);
                for (int k = 0; k < detail.getDesignedProduct().getBluePrints().get(j).getDesignInfos().size(); k++) {
                    DesignInfo designInfo = detail.getDesignedProduct().getBluePrints().get(j).getDesignInfos().get(k);
                    PrintingDesignInfo printingDesignInfo = PrintingDesignInfo.builder().printingBluePrint(printingBluePrint).name(designInfo.getName()).types(designInfo.getTypes()).height(designInfo.getHeight()).width(designInfo.getWidth()).leftPosition(designInfo.getLeftPosition()).topPosition(designInfo.getTopPosition()).rotate(designInfo.getRotate()).scales(designInfo.getScales()).font(designInfo.getFont()).textColor(designInfo.getTextColor()).src(designInfo.getSrc()).build();
                    printingDesignInfos.add(printingDesignInfo);
                }
                printingBluePrints.add(printingBluePrint);
            }

            printingInfo.setPrintingBluePrints(printingBluePrints);
            printingInfo.setPreviewImages(printingImagePreviews);
        return printingInfo;
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
            boolean check = true;
            List<ShippingInfo> shippingInfos = shippingInfoRepository.findAllByUserId(getCredential().getUser().getId());
            for (int i = 0; i < shippingInfos.size(); i++) {
                if (shippingInfos.get(i).getPhoneNumber().equals(shippingInfoDto.getPhone())) {
                    check = false;
                }
            }
            if (check) {
                shippingInfoRepository.save(shippingInfo);
            }
        }

        List<CartDetail> cartDetailList = cart.getCartDetails();
        if (cartDetailList.isEmpty()) throw new CartNotFoundException(ErrorMessage.CART_EMPTY);
        Credential currentCredential = getCredential();
        double totalPrice = 0;
        String address = shippingInfo.getShippingAddress();
        String phone = shippingInfo.getPhoneNumber();
//        String customerName = currentCredential.getUser().getLastName() + " " + currentCredential.getUser().getFirstName();
        String customerName = shippingInfo.getName();
        Orders order = Orders.builder().address(address).customerName(customerName).phone(phone).user(currentCredential.getUser()).build();
        List<OrderDetail> orderDetails = new ArrayList<>();
        List<PrintingInfo> printingInfos = new ArrayList<>();
        for (int i = 0; i < cartDetailList.size(); i++) {
            if (cartDetailList.get(i).getDesignedProduct().getProduct().isDeleted()) {
                throw new ProductNotFoundException(ProductErrorMessage.PRODUCT_NOT_SUPPORT_FOR_ORDER);
            }
            if (!cartDetailList.get(i).getDesignedProduct().isPublish()) {
                if (!cartDetailList.get(i).getDesignedProduct().getUser().getId().equals(currentCredential.getUser().getId())) {
                    throw new ProductNotFoundException(ProductErrorMessage.PRODUCT_NOT_SUPPORT_FOR_ORDER);
                }
            }
            OrderStatus orderStatus = OrderStatus.builder().name(OrderState.PENDING).build();
            OrderDetail orderDetail = OrderDetail.builder()
                .orders(order)
                .orderStatuses(Arrays.asList(orderStatus))
                .quantity(cartDetailList.get(i).getQuantity())
                .color(cartDetailList.get(i).getColor())
                .size(cartDetailList.get(i).getSize())
                .factory(cartDetailList.get(i).getDesignedProduct().getPriceByFactory().getFactory())
                .designedProduct(cartDetailList.get(i).getDesignedProduct())
                .build();
            //save design for printing if there's any edit to the design will have no effect
            printingInfos.add(saveDesignForPrinting(cartDetailList.get(i), orderDetail));
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
//            sizeColorByFactories.add(sizeColorByFactory);
            orderDetails.add(orderDetail);
            totalPrice += cartDetailList.get(i).getQuantity() * (cartDetailList.get(i).getDesignedProduct().getPriceByFactory().getPrice() + cartDetailList.get(i).getDesignedProduct().getDesignedPrice());
        }
        if (totalPrice < 1000 || totalPrice > 50000000)
            throw new PermissionException(ValidationMessage.PRICE_TOTAL_SHOULD_VALID);

//        sizeColorByFactoryRepository.saveAll(sizeColorByFactories);
        order.setOrderDetails(orderDetails);
        order.setPrice(totalPrice);
        order.setPaid(false);
        order.setRefunded(false);
        order.setTransactionId("");
        ordersRepository.save(order);
        printingInfoRepository.saveAll(printingInfos);
//        cartRepository.delete(cart);
        cartDetailRepository.deleteAllInBatch(cartDetailList);
        // create payment info
        PaymentResponse paymentResponse = null;
        String orderInfo = String.format("Total : %f  , Phone : %s", order.getPrice(), order.getPhone());
        String requestId = String.valueOf(System.currentTimeMillis());
        String orderId = String.valueOf(System.currentTimeMillis());
        Double amount = order.getOrderDetails().stream().filter(orderDetail -> !orderDetail.isCanceled()).collect(Collectors.toList())
            .stream()
            .mapToDouble(orderDetail -> orderDetail.getQuantity() * (orderDetail.getDesignedProduct().getDesignedPrice() + orderDetail.getDesignedProduct().getPriceByFactory().getPrice()))
            .sum();
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

        if (paymentResponse.getMessage().equals("[System] Hệ thống đang có lỗi, vui lòng quay lại sau.")) {
            throw new IllegalStateException("ZALO_SYSTEM_ERROR");
        }

        return paymentResponse;
    }

//    @Override
//    @Transactional
//    public void cancelOrder(CancelOrderByUserDto dto) throws IOException {
//        Orders orders = ordersRepository.findById(dto.getOrderId()).orElseThrow(
//            () -> new OrderNotFoundException(OrderErrorMessage.ORDER_NOT_FOUND_EXCEPTION));
//        if (!orders.canCancel()) {
//            throw new IllegalStateException("CAN NOT CANCEL THIS ORDER ");
//        }
//        orders.setCanceled(true);
//        orders.setCancelReason(dto.getCancelReason());
//        for (int i = 0; i < orders.getOrderDetails().size(); i++) {
//            orders.getOrderDetails().get(i).setReasonByUser(dto.getCancelReason());
//            orders.getOrderDetails().get(i).setCanceled(true);
//        }
//        try {
//            if (orders.isPaid() && !orders.isRefunded()) {
//                if (orders.getTransactionId().contains("_")) {
//                    // zalo pay transactionId has '_'
//                    List<OrderDetail> orderDetails = orders.getOrderDetails();
//                    Double amountRefund = orderDetails.stream().filter(orderDetail -> !orderDetail.isCancel()).collect(Collectors.toList())
//                        .stream().mapToDouble(orderDetail ->
//                            orderDetail.getQuantity() * (orderDetail.getDesignedProduct().getDesignedPrice() + orderDetail.getDesignedProduct()
//                                .getPriceByFactory().getPrice())).sum();
//                    zaloService.refund(Double.valueOf(amountRefund).longValue(), String.format("Refund order: %s", dto.getOrderId()), orders.getAppTransId());
//                } else {
//                    //momo transaction
//                }
//
//            }
//        } catch (Exception ex) {
//            throw new RefundException(ex.getMessage());
//        }
//
//        orders.setRefunded(true);
//        ordersRepository.save(orders);
//        //add back quantity when user cancel order
//        addBackQuantityWhenCancelingOrderByUser(orders.getId());

    //        if(!getCredential().getUser().getId().equals(orders.getUser().getId())) throw new PermissionException(CommonMessage.PERMISSION_EXCEPTION);
//        if(orders.isPaid()) throw new OrderNotFoundException(OrderErrorMessage.ORDER_PAID_EXCEPTION);
//        List<OrderDetail> orderDetails = orderDetailRepository.findAllByOrders(orders);
//        List<PrintingInfo> printingInfos = new ArrayList<>();
//        List<PrintingImagePreview> printingImagePreviews = new ArrayList<>();
//        List<PrintingBluePrint> printingBluePrints = new ArrayList<>();
//        List<PrintingPlaceholder> printingPlaceholders = new ArrayList<>();
//        List<PrintingDesignInfo> printingDesignInfos = new ArrayList<>();
//        for (int i = 0; i < orderDetails.size() ; i++) {
//            printingInfos.add(orderDetails.get(i).getPrintingInfo());
//        }
//        for (int i = 0; i < printingInfos.size(); i++) {
//            for (int j = 0; j < printingInfos.get(i).getPreviewImages().size(); j++) {
//                printingImagePreviews.add(printingInfos.get(i).getPreviewImages().get(j));
//            }
//            for (int j = 0; j < printingInfos.get(i).getPrintingBluePrints().size(); j++) {
//                printingBluePrints.add(printingInfos.get(i).getPrintingBluePrints().get(j));
//            }
//        }
//        for (int i = 0; i < printingBluePrints.size(); i++) {
//            printingPlaceholders.add(printingBluePrints.get(i).getPrintingPlaceholder());
//            for (int j = 0; j < printingBluePrints.get(i).getPrintingDesignInfos().size(); j++) {
//                printingDesignInfos.add(printingBluePrints.get(i).getPrintingDesignInfos().get(j));
//
//            }
//        }
//        printingDesignInfoRepository.deleteAllInBatch(printingDesignInfos);
//        printingPlaceHolderRepository.deleteAllInBatch(printingPlaceholders);
//        printingBluePrintRepository.deleteAllInBatch(printingBluePrints);
//        printingImageRepository.deleteAllInBatch(printingImagePreviews);
//        printingInfoRepository.deleteAllInBatch(printingInfos);
//        ordersRepository.delete(orders);
//    }

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
    public void completeOrder(String paymentId, String appTransId) {
        //set appTransId and update status of order
        Orders orders = ordersRepository.findByTransactionId(paymentId).orElseThrow(
            () -> new EntityNotFoundException(EntityName.ORDERS + ErrorMessage.NOT_FOUND)
        );
        if (orders.isPaid()) {
            throw new IllegalStateException(EntityName.ORDERS + ErrorMessage.HAS_PAID);
        }
        orders.setAppTransId(appTransId);
        orders.setPaid(true);
        ordersRepository.save(orders);

        //remove cart detail
        //minus quantity
        List<SizeColorByFactory> sizeColorByFactories = new ArrayList<>();
        for (int i = 0; i < orders.getOrderDetails().size(); i++) {
            OrderDetail orderDetail = orders.getOrderDetails().get(i);
            Optional<SizeColor> sizeColor = sizeColorRepository
                .findByColorNameAndSizeNameAndProductId(orderDetail.getColor(), orderDetail.getSize(), orderDetail.getDesignedProduct().getProduct().getId());
            Optional<SizeColorByFactory> sizeColorByFactory = sizeColorByFactoryRepository.findByFactoryAndSizeColor(orderDetail.getDesignedProduct().getPriceByFactory().getFactory(), sizeColor.get());
            sizeColorByFactory.get().setQuantity(sizeColorByFactory.get().getQuantity() - orders.getOrderDetails().get(i).getQuantity());
            sizeColorByFactories.add(sizeColorByFactory.get());
        }
        sizeColorByFactoryRepository.saveAll(sizeColorByFactories);
    }

    private void addBackQuantityWhenCancelingOrderByUser(String orderId) {
        Orders orders = ordersRepository.findById(orderId).orElseThrow(
            () -> new EntityNotFoundException(EntityName.ORDERS + ErrorMessage.NOT_FOUND)
        );
        List<OrderDetail> orderDetails = orders.getOrderDetails().stream().filter(orderDetail -> !orderDetail.isCancel()).collect(Collectors.toList());
        List<SizeColorByFactory> sizeColorByFactories = new ArrayList<>();
        for (int i = 0; i < orderDetails.size(); i++) {
            OrderDetail orderDetail = orderDetails.get(i);
            Optional<SizeColor> sizeColor = sizeColorRepository
                .findByColorNameAndSizeNameAndProductId(orderDetail.getColor(), orderDetail.getSize(), orderDetail.getDesignedProduct().getProduct().getId());
            Optional<SizeColorByFactory> sizeColorByFactory = sizeColorByFactoryRepository.findByFactoryAndSizeColor(orderDetail.getDesignedProduct().getPriceByFactory().getFactory(), sizeColor.get());
            sizeColorByFactory.get().setQuantity(sizeColorByFactory.get().getQuantity() + orderDetails.get(i).getQuantity());
            sizeColorByFactories.add(sizeColorByFactory.get());
        }
        sizeColorByFactoryRepository.saveAll(sizeColorByFactories);
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
    public PageDTO getAllOrder(String email, Pageable pageable, Boolean isPaid, Boolean cancel) {
        Credential credential = credentialRepository.findCredentialByEmail(email).orElseThrow(
            () -> new CredentialNotFoundException(EntityName.CREDENTIAL + ErrorMessage.NOT_FOUND)
        );
        User user = credential.getUser();
        Page<Orders> ordersPage = null;

        if (isPaid != null) {
            if (cancel != null) {
                ordersPage = ordersRepository.findAllByUserAndIsPaidAndCanceled(pageable, user, isPaid, cancel);
            } else {
                ordersPage = ordersRepository.findAllByUserAndIsPaid(pageable, user, isPaid);
            }
        } else {
            if (cancel != null) {
                ordersPage = ordersRepository.findAllByUserAndCanceled(pageable, user, cancel);
            } else {
                ordersPage = ordersRepository.findAllByUser(pageable, user);
            }
        }
        PageDTO pageDTO = new PageDTO();
        pageDTO.setPage(pageable.getPageNumber());
        pageDTO.setElements(Long.valueOf(ordersPage.getTotalElements()).intValue());
        List<AllOrderDto> orderDtos = ordersPage.stream().map(orders -> {
            List<OrderDetail> orderDetails = orders.getOrderDetails();
            return AllOrderDto.builder()
                .orderId(orders.getId()).cancelReasonByUser(orders.getCancelReason())
                .countItem(orderDetails.stream().mapToInt(OrderDetail::getQuantity).sum())
                .isPaid(orders.isPaid())
                .totalBill(orders.getPrice())
                .createdDate(orders.getCreateDate())
                .canCancel(orders.canCancel())
                .canceled(orders.isCanceled())
                .orderDetailDtos(orders.getOrderDetails().stream()
                    .map(orderDetail -> orderDetailConverter.entityToMyOrderDetailDto(orderDetail))
                    .collect(Collectors.toList()))
                .build();
        }).collect(Collectors.toList());
        pageDTO.setData(orderDtos);
        return pageDTO;
    }

    @Override
    public PaymentResponse payOrder(int paymentMethod, String orderID) throws Exception {
        Orders orders = ordersRepository.findById(orderID).orElseThrow(
            () -> new EntityNotFoundException(EntityName.ORDERS + ErrorMessage.NOT_FOUND)
        );
        if (orders.isPaid()) {
            throw new IllegalStateException(EntityName.ORDERS + ErrorMessage.HAS_PAID);
        }
        PaymentResponse paymentResponse = null;
        String orderInfo = String.format("Total : %f  , Phone : %s", orders.getPrice(), orders.getPhone());
        String requestId = String.valueOf(System.currentTimeMillis());
        String orderId = String.valueOf(System.currentTimeMillis());
        Double amount = orders.getOrderDetails().stream().filter(orderDetail -> !orderDetail.isCanceled()).collect(Collectors.toList())
            .stream()
            .mapToDouble(orderDetail -> orderDetail.getQuantity() * (orderDetail.getDesignedProduct().getDesignedPrice() + orderDetail.getDesignedProduct().getPriceByFactory().getPrice()))
            .sum();
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


    public PageDTO getAllMyOrderDetail(int page, int size) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentCredentialId = (String) authentication.getCredentials();
        Credential credential = credentialRepository.findById(currentCredentialId)
            .orElseThrow(() -> new CredentialNotFoundException(CredentialErrorMessage.CREDENTIAL_NOT_FOUND_EXCEPTION));

        AllOrderDetail allOrderDetail =
            orderDetailRepository.findAllOrderDetailIsPaidTrueOrderDetail(page + 1, size, credential.getUser().getId());
        return PageDTO.builder()
            .data(allOrderDetail.getResult()
                .stream()
                .map(orderDetail -> orderDetailConverter.entityToMyOrderDetailDto(orderDetail))
                .collect(Collectors.toList()))
            .elements(allOrderDetail.getElements())
            .page(page)
            .build();

    }

    @Override
    @Transactional
    public PaymentResponse orderOwnDesign(OrderOwnDesignDto orderOwnDesignDto, int paymentMethod) throws Exception {
        Cart cart = cartRepository.findCartByUser(getCredential().getUser());
        if (cart == null) throw new CartNotFoundException(CartErrorMessage.CART_NOT_FOUND_ERROR);
        if (cart.getUser().getId() != getCredential().getUser().getId()) {
            throw new PermissionException(CommonMessage.PERMISSION_EXCEPTION);
        }
        DesignedProduct designedProduct = designedProductRepository.findById(orderOwnDesignDto.getDesignId())
            .orElseThrow(() -> new EntityNotFoundException(EntityName.DESIGNED_PRODUCT + ErrorMessage.NOT_FOUND)
            );

        CartDetail savedCartDetail = cartDetailRepository.save(
            CartDetail.builder()
                .cart(cart)
                .size(orderOwnDesignDto.getSize())
                .color(orderOwnDesignDto.getColor())
                .quantity(orderOwnDesignDto.getQuantity())
                .designedProduct(designedProduct)
                .build());

        ShippingInfo shippingInfo = ShippingInfo.builder()
            .phoneNumber(orderOwnDesignDto.getPhone())
            .name(orderOwnDesignDto.getName())
            .shippingAddress(orderOwnDesignDto.getAddress())
            .emailAddress(orderOwnDesignDto.getEmail())
            .user(getCredential().getUser())
            .build();
        if (orderOwnDesignDto.isShouldSave()) {
            shippingInfoRepository.save(shippingInfo);
        }
        List<CartDetail> cartDetailList = Arrays.asList(savedCartDetail);
        if (cartDetailList.isEmpty()) throw new CartNotFoundException(ErrorMessage.CART_EMPTY);
        Credential currentCredential = getCredential();
        double totalPrice = 0;
        String address = currentCredential.getAddress();
        String phone = currentCredential.getPhone();
        String customerName = currentCredential.getUser().getLastName() + " " + currentCredential.getUser().getFirstName();
        Orders order = Orders.builder().address(address).customerName(customerName).phone(phone).user(currentCredential.getUser()).build();
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
            OrderStatus orderStatus = OrderStatus.builder().name(OrderState.PENDING).build();
            OrderDetail orderDetail = OrderDetail.builder()
                .orders(order)
                .orderStatuses(Arrays.asList(orderStatus))
                .quantity(cartDetailList.get(i).getQuantity())
                .color(cartDetailList.get(i).getColor())
                .size(cartDetailList.get(i).getSize())
                .factory(cartDetailList.get(i).getDesignedProduct().getPriceByFactory().getFactory())
                .designedProduct(cartDetailList.get(i).getDesignedProduct())
                .build();
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
        cartDetailRepository.deleteById(savedCartDetail.getId());
        // create payment info
        PaymentResponse paymentResponse = null;
        String orderInfo = String.format("Total : %f  , Phone : %s", order.getPrice(), order.getPhone());
        String requestId = String.valueOf(System.currentTimeMillis());
        String orderId = String.valueOf(System.currentTimeMillis());
        Double amount = order.getOrderDetails().stream().filter(orderDetail -> !orderDetail.isCanceled()).collect(Collectors.toList())
            .stream()
            .mapToDouble(orderDetail -> orderDetail.getQuantity() * (orderDetail.getDesignedProduct().getDesignedPrice() + orderDetail.getDesignedProduct().getPriceByFactory().getPrice()))
            .sum();
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

    @Override
    public DesignerDashboard getDesignerDashboard(LocalDateTime startDate, LocalDateTime endDate) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentCredentialId = (String) authentication.getCredentials();
        Credential credential = credentialRepository.findById(currentCredentialId.toString()).orElseThrow(() -> new CredentialNotFoundException(CredentialErrorMessage.CREDENTIAL_NOT_FOUND_EXCEPTION));
        User user = credential.getUser();
        Double income = ordersRepository.getInComeByUserId(user.getId(), startDate, endDate) * 0.8;
        Double incomeCurrentMonth = ordersRepository.getInComeByUserId(user.getId(), LocalDateTime.now().withDayOfMonth(1), endDate) * 0.8;
        long designCount = designedProductRepository.countAllByUser(user);
        long designSoldCount = ordersRepository.countSoldByUserId(user.getId(), startDate, endDate);
        long designSoldCountCurrentMonth = ordersRepository.countSoldByUserId(user.getId(), LocalDateTime.now().withDayOfMonth(1), endDate);
        return DesignerDashboard.builder()
            .income(income)
            .designSoldCount(designSoldCount)
            .designCount(designCount)
            .incomeCurrentMonth(incomeCurrentMonth)
            .designSoldCountCurrentMonth(designSoldCountCurrentMonth)
            .build();
    }

    @Override
    public AdminDashboard getAdminDashboard(LocalDateTime startDate, LocalDateTime endDate) {
        List<CategorySoldCountProjection> categorySoldCountProjections = orderDetailRepository.countOrderByCategory();
        Double income = ordersRepository.getInComeByAdmin(startDate, endDate) / 100;
        Double incomeCurrentMonth = ordersRepository.getInComeByAdmin(LocalDateTime.now().withDayOfMonth(1), endDate) / 100;

        Long countZaloOrder = ordersRepository.countZaloPay();
        Long orderCount = ordersRepository.countByIsPaid(true);
        return AdminDashboard.builder()
            .orderCount(orderCount)
            .orderCountCurrentMonth(ordersRepository.countSoldAll(LocalDateTime.now().withDayOfMonth(1), endDate))
            .income(income)
            .incomeCurrentMonth(incomeCurrentMonth)
            .designCount(designedProductRepository.count())
            .designSoldCount(ordersRepository.countSoldAll(LocalDateTime.now().withDayOfMonth(1), endDate))
            .countMomoOrder(orderCount - countZaloOrder)
            .countZaloPayOrder(countZaloOrder)
            .categorySoldCountProjections(categorySoldCountProjections)
            .build();
    }

    @Override
    public FactoryDashboard getFactoryDashboard(LocalDateTime startDate, LocalDateTime endDate) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentCredentialId = (String) authentication.getCredentials();
        Credential credential = credentialRepository.findById(currentCredentialId.toString()).orElseThrow(
            () -> new CredentialNotFoundException(CredentialErrorMessage.CREDENTIAL_NOT_FOUND_EXCEPTION));
        Factory factory = credential.getFactory();

        Double income = ordersRepository.getInComeByFactory(factory.getId(), startDate, endDate) * (100 - factory.getTradeDiscount()) / 100;
        Double incomeCurrentMonth = ordersRepository.getInComeByFactory(factory.getId(), LocalDateTime.now().withDayOfMonth(1), endDate) * (100 - factory.getTradeDiscount()) / 100;
        List<OrderDetail> orderDetails = orderDetailRepository.findAllByFactory(factory);
        long isDone = orderDetails.stream()
            .filter(orderDetail -> orderDetail.getOrders().isPaid())
            .filter(orderDetail -> !orderDetail.getOrders().isRefunded())
            .filter(orderDetail -> orderDetail.isDone()).count();

        long isInProcess = orderDetails.stream()
            .filter(orderDetail -> orderDetail.getOrders().isPaid())
            .filter(orderDetail -> !orderDetail.getOrders().isRefunded())
            .filter(orderDetail -> !orderDetail.isDone())
            .filter(orderDetail -> !orderDetail.isCanceled())
            .count();

        return FactoryDashboard.builder()
            .income(income)
            .incomeCurrentMonth(incomeCurrentMonth)
            .inProcessOrder(isInProcess)
            .doneOrder(isDone)
            .build();
    }

    @Override
    public void updateOrderDetailsStatus(List<String> orderDetailIds, String orderStatus) {
        List<OrderDetail> orderDetails = new ArrayList<>();
        for (int i = 0; i < orderDetailIds.size(); i++) {
            OrderDetail orderDetail = orderDetailRepository.findById(orderDetailIds.get(i)).orElseThrow(() -> new OrderNotFoundException(OrderErrorMessage.ORDER_NOT_FOUND_EXCEPTION));
            orderDetails.add(orderDetail);
        }
        for (int i = 0; i < orderDetails.size(); i++) {
            for (int j = 0; j < OrderState.getAllOrderState().size(); j++) {
                if (orderStatus.equals(OrderState.getAllOrderState().get(j))) {
                    orderDetails.get(i).getOrderStatuses().add(OrderStatus.builder().orderDetail(orderDetails.get(i)).name(orderStatus).build());
                }
            }
        }
        orderDetailRepository.saveAll(orderDetails);
    }

    @Override
    public List<MyOrderDetailDto> getOderDetailByOrderId(String orderId) {
        Orders orders = ordersRepository.findById(orderId).orElseThrow(
            () -> new EntityNotFoundException(EntityName.ORDERS + "_" + ErrorMessage.NOT_FOUND)
        );
        return orderDetailRepository.findAllByOrders(orders)
            .stream()
            .map(orderDetail -> orderDetailConverter.entityToMyOrderDetailDto(orderDetail))
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void cancelOrderDetail(CancelOrderDto dto) {
        Credential credential = getCredential();

        List<OrderDetail> orderDetails = new ArrayList<>();
        for (int i = 0; i < dto.getOrderDetailIds().size(); i++) {
            OrderDetail orderDetail = orderDetailRepository.findById(dto.getOrderDetailIds().get(i)).orElseThrow(() -> new OrderNotFoundException(OrderErrorMessage.ORDER_NOT_FOUND_EXCEPTION));
            orderDetails.add(orderDetail);
        }
        if (orderDetails.isEmpty()) throw new OrderNotFoundException(OrderErrorMessage.ORDER_NOT_FOUND_EXCEPTION);
        boolean isUser = true;
        if (credential.getFactory() != null) {
            if (!orderDetails.get(0).getFactory().getId().equals(getCredential().getFactory().getId())) {
                throw new PermissionException(CommonMessage.PERMISSION_EXCEPTION);
            }
            isUser = false;
        } else {
            for (int i = 0; i < orderDetails.size(); i++) {
                if (!orderDetails.get(i).getOrders().getUser().getId().equals(credential.getUser().getId())) {
                    throw new PermissionException(CommonMessage.PERMISSION_EXCEPTION);
                }
            }
        }
        if (isUser) {
            for (int i = 0; i < orderDetails.size(); i++) {
                List<OrderStatus> orderStatuses = orderDetails.get(i).getOrderStatuses();
                if (orderStatuses.size() > 1) {
                    throw new PermissionException(OrderErrorMessage.ORDER_CANCEL_ERROR_EXCEPTION);
                } else {
                    if (orderDetails.get(i).isCancel())
                        throw new OrderStatusNotFoundException(OrderErrorMessage.ORDER_HAS_BEEN_CANCEL_EXCEPTION);
                    orderStatuses.add(OrderStatus.builder().name(OrderState.CANCEL).orderDetail(orderDetails.get(i)).build());
                    orderDetails.get(i).setOrderStatuses(orderStatuses);
                    orderDetails.get(i).setCanceled(true);
                    orderDetails.get(i).setReasonByUser(dto.getCancelReason());
                }
            }
        } else {
            for (int i = 0; i < orderDetails.size(); i++) {
                if (orderDetails.get(i).isCancel())
                    throw new OrderStatusNotFoundException(OrderErrorMessage.ORDER_HAS_BEEN_CANCEL_EXCEPTION);
                List<OrderStatus> orderStatuses = orderDetails.get(i).getOrderStatuses();
                orderStatuses.add(OrderStatus.builder().name(OrderState.CANCEL).orderDetail(orderDetails.get(i)).build());
                orderDetails.get(i).setOrderStatuses(orderStatuses);
                orderDetails.get(i).setCanceled(true);
                orderDetails.get(i).setReasonByFactory(dto.getCancelReason());
            }
        }

        Orders orders = orderDetails.get(0).getOrders();
        String cancelReason = orderDetails.get(0).getReasonByUser() != null ? orderDetails.get(0).getReasonByUser() : orderDetails.get(0).getReasonByFactory();
        try {
            if (orders.isPaid()) {
                if (orders.getTransactionId().contains("_")) {
                    Double amountRefund = orderDetails.stream()
                        .mapToDouble(orderDetail ->
                            orderDetail.getQuantity() * (orderDetail.getDesignedProduct().getDesignedPrice() + orderDetail.getDesignedProduct()
                                .getPriceByFactory().getPrice())).sum();
                    // zalo pay transactionId has '_'
                    zaloService.refund(Double.valueOf(amountRefund).longValue(), String.format("Refund order: %s ,\n Reason %s", orderDetails.get(0).getOrders().getId(), cancelReason), orders.getAppTransId());
                } else {
                    //momo transaction
                }

            }
        } catch (Exception ex) {
            throw new RefundException(ex.getMessage());
        }
        orderDetailRepository.saveAll(orderDetails);
        entityManager.flush();
        // if all orderDetail is canceled so set orders is canceled
        Orders orderAfterUpdate = ordersRepository.findById(orderDetails.get(0).getOrders().getId()).orElseThrow(
            () -> new EntityNotFoundException(EntityName.ORDERS + "_" + ErrorMessage.NOT_FOUND)
        );

        long amountOfCanceledOrderDetails = orderAfterUpdate.getOrderDetails()
            .stream()
            .filter(orderDetail -> orderDetail.latestStatus().equals(OrderState.CANCEL))
            .count();
        if (amountOfCanceledOrderDetails == orderAfterUpdate.getOrderDetails().size()) {
            ordersRepository.setOrderIsCanceled(orderAfterUpdate.getId());
        }

        addBackQuantityWhenCancelingOrderByFactory(orderDetails);
    }

    private void addBackQuantityWhenCancelingOrderByFactory(List<OrderDetail> orderDetails) {
        List<SizeColorByFactory> sizeColorByFactories = new ArrayList<>();
        for (int i = 0; i < orderDetails.size(); i++) {
            OrderDetail orderDetail = orderDetails.get(i);
            Optional<SizeColor> sizeColor = sizeColorRepository
                .findByColorNameAndSizeNameAndProductId(orderDetail.getColor(), orderDetail.getSize(), orderDetail.getDesignedProduct().getProduct().getId());
            Optional<SizeColorByFactory> sizeColorByFactory = sizeColorByFactoryRepository.findByFactoryAndSizeColor(orderDetail.getDesignedProduct().getPriceByFactory().getFactory(), sizeColor.get());
            sizeColorByFactory.get().setQuantity(sizeColorByFactory.get().getQuantity() + orderDetails.get(i).getQuantity());
            sizeColorByFactories.add(sizeColorByFactory.get());
        }
        sizeColorByFactoryRepository.saveAll(sizeColorByFactories);
    }
}
