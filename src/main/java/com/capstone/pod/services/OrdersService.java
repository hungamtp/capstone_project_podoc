package com.capstone.pod.services;


import com.capstone.pod.dto.order.ReturnOrderDto;
import com.capstone.pod.dto.order.ShippingInfoDto;
import com.capstone.pod.momo.models.PaymentResponse;

import java.util.List;

public interface OrdersService {
    PaymentResponse addOrder(ShippingInfoDto shippingInfoDto,int paymentMethod) throws Exception;
    void setPaymentIdForOrder(String orderId , String paymentId);
    void completeOrder(String paymentId);

    List<ShippingInfoDto> getMyShippingInfo();
}
