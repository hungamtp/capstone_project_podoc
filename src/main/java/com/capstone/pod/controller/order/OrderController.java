package com.capstone.pod.controller.order;

import com.capstone.pod.constant.order.OrderSuccessMessage;
import com.capstone.pod.constant.role.RolePreAuthorize;
import com.capstone.pod.dto.common.PageDTO;
import com.capstone.pod.dto.common.ResponseDto;
import com.capstone.pod.dto.order.OrderOwnDesignDto;
import com.capstone.pod.dto.order.ShippingInfoDto;
import com.capstone.pod.dto.utils.Utils;
import com.capstone.pod.momo.config.Environment;
import com.capstone.pod.momo.enums.RequestType;
import com.capstone.pod.momo.models.PaymentResponse;
import com.capstone.pod.momo.processor.CreateOrderMoMo;
import com.capstone.pod.momo.shared.utils.LogUtils;
import com.capstone.pod.services.OrdersService;
import com.capstone.pod.services.PayService;
import com.capstone.pod.zalo.ZaloService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("order")
@RequiredArgsConstructor
public class OrderController {
    private final OrdersService ordersService;
    private final PayService payService;
    private final ZaloService zaloService;

    @PostMapping("/{paymentMethod}")
    @PreAuthorize(RolePreAuthorize.ROLE_USER)
    public ResponseEntity<ResponseDto> addOrder(@Validated @RequestBody ShippingInfoDto shippingInfoDto , @PathVariable int paymentMethod) throws Exception {
        LogUtils.init();
        ResponseDto responseDTO = new ResponseDto();
        PaymentResponse paymentResponse = ordersService.addOrder(shippingInfoDto , paymentMethod);
        responseDTO.setData(paymentResponse);
        return ResponseEntity.ok().body(responseDTO);
    }
    @GetMapping("/shippinginfos")
    @PreAuthorize(RolePreAuthorize.ROLE_USER)
    public ResponseEntity<com.capstone.pod.dto.http.ResponseDto> getShippingInfos(){
        com.capstone.pod.dto.http.ResponseDto responseDto= new com.capstone.pod.dto.http.ResponseDto();
        List<ShippingInfoDto> shippingInfoDtos = ordersService.getMyShippingInfo();
        responseDto.setData(shippingInfoDtos);
        responseDto.setSuccessMessage(OrderSuccessMessage.GET_SHIPPING_INFO_SUCCESS);
        return ResponseEntity.ok().body(responseDto);
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

        //ZaloPay
        //amount=323123
        // &discountamount=0
        // &appid=2553&checksum=bb1ee529210b36f63221938bb50a4e1a9fa5566ef294ca4aa70731cd963de033
        // &apptransid=220713_695232
        // &pmcid=38
        // &bankcode=&status=1
        ordersService.completeOrder(orderId);
        return ResponseEntity.ok().body("PAID_SUCCESS");
    }

    @GetMapping("/myorder")
    @PreAuthorize(RolePreAuthorize.ROLE_USER)
    public ResponseEntity getAllMyOrder(HttpServletRequest request , @RequestParam int page , @RequestParam int size) {
        String jwt = request.getHeader("Authorization");
        String email = Utils.getEmailFromJwt(jwt.replace("Bearer ", ""));
        Pageable pageable = PageRequest.of(page, size);
        PageDTO pageDTO = ordersService.getAllOrderIsNotPaid(email, pageable);
        return ResponseEntity.ok().body(pageDTO);
    }

    @GetMapping("/orderdetail")
    public ResponseEntity getAllOrderDetail(@RequestParam int page , @RequestParam int size){
        if(page <1){
            throw new IllegalStateException("PAGE > 0");
        }
        var result = ordersService.getAllMyOrderDetail(page , size);
        PageDTO pageDTO = new PageDTO();
        pageDTO.setElements(result.size());
        pageDTO.setData(result);
        pageDTO.setPage(page);
        return ResponseEntity.ok().body(pageDTO);
    }

    @PutMapping
    public ResponseEntity payUnpaidOrder(@RequestParam int paymentMethod , @RequestParam String orderId) throws Exception {
        return ResponseEntity.ok().body(ordersService.payOrder(paymentMethod , orderId));
    }

    @PostMapping("/orderOwnDesign/{paymentMethod}")
    public ResponseEntity orderOwnDesign(@PathVariable int paymentMethod , @RequestBody OrderOwnDesignDto orderOwnDesignDto) throws Exception {
        return ResponseEntity.ok().body(ordersService.orderOwnDesign(orderOwnDesignDto , paymentMethod));
    }
}
