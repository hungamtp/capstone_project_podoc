package com.capstone.pod.services;


import com.capstone.pod.dto.common.PageDTO;
import com.capstone.pod.dto.order.MyOrderDetailDto;
import com.capstone.pod.dto.order.ReturnOrderDto;
import com.capstone.pod.dto.order.ShippingInfoDto;
import com.capstone.pod.momo.models.PaymentResponse;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface OrdersService {
    PaymentResponse addOrder(ShippingInfoDto shippingInfoDto, int paymentMethod) throws Exception;

    void completeOrder(String paymentId);

    List<ShippingInfoDto> getMyShippingInfo();

    PageDTO getAllOrderIsNotPaid(String email, Pageable pageable);

    PaymentResponse payOrder(int paymentMethod ,String orderId) throws Exception;

    List<MyOrderDetailDto> getAllMyOrderDetail(int page, int size);

}
