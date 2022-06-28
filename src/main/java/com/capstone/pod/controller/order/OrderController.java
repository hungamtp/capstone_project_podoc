package com.capstone.pod.controller.order;

import com.capstone.pod.constant.order.OrderSuccessMessage;
import com.capstone.pod.constant.role.RolePreAuthorize;
import com.capstone.pod.dto.http.ResponseDto;
import com.capstone.pod.dto.order.ReturnOrderDTO;
import com.capstone.pod.momo.config.Environment;
import com.capstone.pod.momo.enums.RequestType;
import com.capstone.pod.momo.models.PaymentResponse;
import com.capstone.pod.momo.processor.CreateOrderMoMo;
import com.capstone.pod.momo.shared.utils.LogUtils;
import com.capstone.pod.services.OrdersService;
import com.capstone.pod.services.PayService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("order")
@RequiredArgsConstructor
public class OrderController {
    private final OrdersService ordersService;
    private final PayService payService;

    @PostMapping
    @PreAuthorize(RolePreAuthorize.ROLE_USER)
    public ResponseEntity<PaymentResponse> addOrder(@RequestParam int cartId) throws Exception {

        ReturnOrderDTO returnOrderDTO = ordersService.addOrder(cartId);
        String orderInfo = new StringBuilder().append(
                returnOrderDTO.getCustomerName()
            )
            .append(returnOrderDTO.getPhone())
            .append(returnOrderDTO.getAddress()).toString();
        return ResponseEntity.ok().body(payService.createOrder(Integer.valueOf(returnOrderDTO.getId()).toString(),Double.valueOf( returnOrderDTO.getPrice()).toString(),orderInfo ,"" , true ));
    }

}
