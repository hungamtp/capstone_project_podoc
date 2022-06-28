package com.capstone.pod.services;


import com.capstone.pod.dto.order.ReturnOrderDTO;

public interface OrdersService {
    public ReturnOrderDTO addOrder(int cartId);

}
