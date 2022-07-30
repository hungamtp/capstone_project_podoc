package com.capstone.pod.services;


import com.capstone.pod.dto.common.PageDTO;
import com.capstone.pod.dto.dashboard.AdminDashboard;
import com.capstone.pod.dto.dashboard.DesignerDashboard;
import com.capstone.pod.dto.dashboard.FactoryDashboard;
import com.capstone.pod.dto.order.MyOrderDetailDto;
import com.capstone.pod.dto.order.OrderOwnDesignDto;
import com.capstone.pod.dto.order.ShippingInfoDto;
import com.capstone.pod.momo.models.PaymentResponse;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface OrdersService {
    PaymentResponse addOrder(ShippingInfoDto shippingInfoDto, int paymentMethod) throws Exception;

    void completeOrder(String paymentId);

    List<ShippingInfoDto> getMyShippingInfo();

    PageDTO getAllOrderIsNotPaid(String email, Pageable pageable);

    PaymentResponse payOrder(int paymentMethod ,String orderId) throws Exception;

    List<MyOrderDetailDto> getAllMyOrderDetail(int page, int size);

    PaymentResponse orderOwnDesign(OrderOwnDesignDto orderOwnDesignDto, int paymentMethod) throws Exception;

    DesignerDashboard getDesignerDashboard(LocalDateTime startDate , LocalDateTime endDate);

    AdminDashboard getAdminDashboard(LocalDateTime startDate, LocalDateTime endDate);

    FactoryDashboard getFactoryDashboard(LocalDateTime startDate, LocalDateTime endDate);

    void updateOrderDetailsStatus(String orderDetailId,String orderStatus);
}
