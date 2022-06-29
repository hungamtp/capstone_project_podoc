package com.capstone.pod.services;

import com.capstone.pod.momo.config.Environment;
import com.capstone.pod.momo.enums.RequestType;
import com.capstone.pod.momo.models.PaymentResponse;

public interface PayService {
    PaymentResponse createOrder(String orderId, String amount, String orderInfo, String extraData,
                                Boolean autoCapture) throws Exception;
}
