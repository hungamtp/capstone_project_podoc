package com.capstone.pod.controller.order;

import com.capstone.pod.constant.order.OrderSuccessMessage;
import com.capstone.pod.constant.role.RolePreAuthorize;
import com.capstone.pod.dto.http.ResponseDto;
import com.capstone.pod.momo.config.Environment;
import com.capstone.pod.momo.enums.RequestType;
import com.capstone.pod.momo.models.PaymentResponse;
import com.capstone.pod.momo.processor.CreateOrderMoMo;
import com.capstone.pod.momo.shared.utils.LogUtils;
import com.capstone.pod.services.OrdersService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("order")
@RequiredArgsConstructor
public class OrderController {
    private final OrdersService ordersService;
    @PostMapping
    @PreAuthorize(RolePreAuthorize.ROLE_USER)
    public ResponseEntity<ResponseDto> addOrder(@RequestParam int cartId){
        ResponseDto<Integer> responseDto = new ResponseDto();
        responseDto.setData(ordersService.addOrder(cartId));
        responseDto.setSuccessMessage(OrderSuccessMessage.ORDER_SUCCESS);
        return ResponseEntity.ok().body(responseDto);
    }

    @GetMapping
    public ResponseEntity  createTran() throws Exception {
        LogUtils.init();
        String requestId = String.valueOf(System.currentTimeMillis());
        String orderId = String.valueOf(System.currentTimeMillis());
        Long transId = 2L;
        long amount = 50000;

        String partnerClientId = "partnerClientId";
        String orderInfo = "Pay With MoMo";
        String returnURL = "https://google.com.vn";
        String notifyURL = "https://google.com.vn";
        String callbackToken = "callbackToken";
        String token = "";

        Environment environment = Environment.selectEnv("dev");
        PaymentResponse captureWalletMoMoResponse = CreateOrderMoMo.process(environment, orderId, requestId, Long.toString(amount), orderInfo, returnURL, notifyURL, "", RequestType.CAPTURE_WALLET, Boolean.TRUE);

        return ResponseEntity.ok().body(captureWalletMoMoResponse);
    }
}
