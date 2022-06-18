package com.capstone.pod.services;

import com.capstone.pod.dto.cartdetail.CartDetailDto;
import com.capstone.pod.dto.cartdetail.CartNotEnoughDto;

import java.util.List;

public interface CartService {
    List<CartDetailDto> getCard(String email);
    void updateCart(List<CartDetailDto> dtos, String email);
    void deleteCartDetail(Integer cartDetailId, String email);
    List<CartNotEnoughDto> checkQuantityBeforeOrder(List<CartDetailDto> cartDetails, String email);
}
