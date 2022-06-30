package com.capstone.pod.controller.order;

import com.capstone.pod.constant.order.OrderSuccessMessage;
import com.capstone.pod.constant.role.RolePreAuthorize;
import com.capstone.pod.dto.common.ResponseDTO;
import com.capstone.pod.dto.http.ResponseDto;
import com.capstone.pod.dto.order.ReturnOrderDto;
import com.capstone.pod.dto.order.ShippingInfoDto;
import com.capstone.pod.momo.config.Environment;
import com.capstone.pod.momo.enums.RequestType;
import com.capstone.pod.momo.models.PaymentResponse;
import com.capstone.pod.momo.processor.CreateOrderMoMo;
import com.capstone.pod.momo.shared.utils.LogUtils;
import com.capstone.pod.services.OrdersService;
import com.capstone.pod.services.PayService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("order")
@RequiredArgsConstructor
public class OrderController {
    private final OrdersService ordersService;
    private final PayService payService;

    @PostMapping
    @PreAuthorize(RolePreAuthorize.ROLE_USER)
    public ResponseEntity<PaymentResponse> addOrder(@RequestParam int cartId,@Validated @RequestBody ShippingInfoDto shippingInfoDto) throws Exception {
        ReturnOrderDto returnOrderDTO = ordersService.addOrder(cartId, shippingInfoDto);
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

        String orderInfo = ow.writeValueAsString(returnOrderDTO);
        String requestId = String.valueOf(System.currentTimeMillis());
        String orderId = String.valueOf(System.currentTimeMillis());
        Double amount = returnOrderDTO.getPrice();
        Environment environment = Environment.selectEnv("dev");
        String returnURL = environment.getMomoEndpoint().getRedirectUrl();
        String notifyURL = environment.getMomoEndpoint().getNotiUrl();

        PaymentResponse paymentResponse = CreateOrderMoMo.process(environment, orderId, requestId, Double.toString(amount), orderInfo, returnURL, notifyURL, "", RequestType.CAPTURE_WALLET, Boolean.TRUE);

        //set paymentId to order
        ordersService.setPaymentIdForOrder(returnOrderDTO.getId(), paymentResponse.getOrderId());
        return ResponseEntity.ok().body(paymentResponse);
    }

    @GetMapping
    public ResponseEntity<PaymentResponse> createOrder() throws Exception {
        LogUtils.init();
        String requestId = String.valueOf(System.currentTimeMillis());
        String orderId = String.valueOf(System.currentTimeMillis());
        long amount = 50000;
        Environment environment = Environment.selectEnv("dev");
        String orderInfo = "Pay With MoMo";
        String returnURL = environment.getMomoEndpoint().getRedirectUrl();
        String notifyURL = environment.getMomoEndpoint().getNotiUrl();

        PaymentResponse captureWalletMoMoResponse = CreateOrderMoMo.process(environment, orderId, requestId, Long.toString(amount), orderInfo, returnURL, notifyURL, "", RequestType.CAPTURE_WALLET, Boolean.TRUE);
        return ResponseEntity.ok().body(captureWalletMoMoResponse);
    }

    @GetMapping("/complete")
    public ResponseEntity completeOrder(@RequestParam String orderId) throws Exception {
        // response from momo

//        ?partnerCode=MOMOQOYK20220626
//            &orderId=1656439230514
//            &requestId=1656439230514
//            &amount=50000
//            &orderInfo=Pay+With+MoMo&orderType=momo_wallet
//            &transId=2694732419
//            &resultCode=0
//            &message=Successful.
//            &payType=qr
//            &responseTime=1656439248218
//            &extraData=
//    &signature=640a8e675597bbf0315f86426e07dee858df920bebc85a56e59db2cd1fe6a6d4
        ordersService.completeOrder(orderId);
        return ResponseEntity.ok().body("captureWalletMoMoResponse");
    }

}
