package com.capstone.pod.services;


import com.capstone.pod.dto.common.PageDTO;
import com.capstone.pod.dto.dashboard.AdminDashboard;
import com.capstone.pod.dto.dashboard.DesignerDashboard;
import com.capstone.pod.dto.dashboard.FactoryDashboard;
import com.capstone.pod.dto.order.CancelOrderDto;
import com.capstone.pod.dto.order.MyOrderDetailDto;
import com.capstone.pod.dto.order.OrderOwnDesignDto;
import com.capstone.pod.dto.order.ShippingInfoDto;
import com.capstone.pod.momo.models.PaymentResponse;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface OrdersService {
    PaymentResponse addOrder(ShippingInfoDto shippingInfoDto, int paymentMethod) throws Exception;
    void cancelOrder(String orderId) ;

    void completeOrder(String paymentId ,String zp_trans_id);

    List<ShippingInfoDto> getMyShippingInfo();

    PageDTO getAllOrder(String email, Pageable pageable , Boolean isPaid , Boolean cancel);

    PaymentResponse payOrder(int paymentMethod ,String orderId) throws Exception;

    PageDTO getAllMyOrderDetail(int page, int size);

    PaymentResponse orderOwnDesign(OrderOwnDesignDto orderOwnDesignDto, int paymentMethod) throws Exception;

    DesignerDashboard getDesignerDashboard(LocalDateTime startDate , LocalDateTime endDate);

    AdminDashboard getAdminDashboard(LocalDateTime startDate, LocalDateTime endDate);

    FactoryDashboard getFactoryDashboard(LocalDateTime startDate, LocalDateTime endDate);

    void updateOrderDetailsStatus(List<String> orderDetailIds,String orderStatus);

    List<MyOrderDetailDto> getOderDetailByOrderId(String orderId);

    void cancelOrderDetailByFactory(CancelOrderDto dto);
}
