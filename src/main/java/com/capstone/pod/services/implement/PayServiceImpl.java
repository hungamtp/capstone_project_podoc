package com.capstone.pod.services.implement;

import com.capstone.pod.momo.config.Environment;
import com.capstone.pod.momo.enums.RequestType;
import com.capstone.pod.momo.models.PaymentResponse;
import com.capstone.pod.momo.processor.CreateOrderMoMo;
import com.capstone.pod.services.PayService;
import org.springframework.stereotype.Service;

@Service
public class PayServiceImpl implements PayService {
    @Override
    public PaymentResponse createOrder(String orderId, String amount, String orderInfo,String extraData
        ,Boolean autoCapture) throws Exception {
        Environment environment = Environment.selectEnv("dev");
        String requestId = String.valueOf(System.currentTimeMillis());
        String returnURL = environment.getMomoEndpoint().getRedirectUrl();;
        String notifyURL = environment.getMomoEndpoint().getNotiUrl();
        RequestType requestType =RequestType.CAPTURE_WALLET;
        return CreateOrderMoMo.process(environment, orderId, requestId, amount, orderInfo, returnURL, notifyURL, "",
            requestType, Boolean.TRUE);
    }
}
