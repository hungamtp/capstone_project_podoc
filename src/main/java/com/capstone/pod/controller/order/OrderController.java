package com.capstone.pod.controller.order;

import com.capstone.pod.constant.order.OrderSuccessMessage;
import com.capstone.pod.dto.http.ResponseDto;
import com.capstone.pod.services.OrdersService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("order")
@RequiredArgsConstructor
public class OrderController {
    private final OrdersService ordersService;
    @PostMapping
    public ResponseEntity<ResponseDto> addOrder(@RequestParam int cartId){
        ResponseDto<Void> responseDto = new ResponseDto();
        ordersService.addOrder(cartId);
        responseDto.setSuccessMessage(OrderSuccessMessage.ORDER_SUCCESS);
        return ResponseEntity.ok().body(responseDto);
    }
}