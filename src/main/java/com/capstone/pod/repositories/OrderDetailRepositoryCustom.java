package com.capstone.pod.repositories;

import com.capstone.pod.entities.OrderDetail;

import java.util.List;

public interface OrderDetailRepositoryCustom {
    List<OrderDetail> findAllOrderDetail(int page, int size, String userId);
}
