package com.capstone.pod.services;

import com.capstone.pod.dto.cartdetail.CartDetailDTO;

import java.util.List;

public interface CartService {
    List<CartDetailDTO> getCard(String email);
}
