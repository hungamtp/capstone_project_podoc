package com.capstone.pod.repositories;

import com.capstone.pod.dto.dashboard.CategorySoldCountProjection;
import com.capstone.pod.entities.OrderDetail;

import java.util.List;

public interface OrderDetailRepositoryCustom {
    List<OrderDetail> findAllOrderDetailIsPaidTrueOrderDetail(int page, int size, String userId);
    List<CategorySoldCountProjection> countOrderByCategory();
}
