package com.capstone.pod.services;


import com.capstone.pod.dto.order.ReturnOrderDto;
import com.capstone.pod.dto.order.ShippingInfoDto;

import java.util.List;

public interface OrdersService {
    public ReturnOrderDto addOrder(ShippingInfoDto shippingInfoDto);
    void setPaymentIdForOrder(String orderId , String paymentId);
    void completeOrder(String paymentId);

    List<ShippingInfoDto> getMyShippingInfo();
}
