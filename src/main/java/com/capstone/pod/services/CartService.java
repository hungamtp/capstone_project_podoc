package com.capstone.pod.services;

import com.capstone.pod.dto.cartdetail.CartDetailDTO;
import com.capstone.pod.dto.cartdetail.CartNotEnough;

import java.util.List;

public interface CartService {
    List<CartDetailDTO> getCard(String email);
    void updateCart(List<CartDetailDTO> dtos, String email);
    void deleteCartDetail(Integer cartDetailId, String email);
    List<CartNotEnough> checkQuantityBeforeOrder(List<CartDetailDTO> cartDetails, String email);
}
