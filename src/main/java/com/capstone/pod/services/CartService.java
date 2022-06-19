package com.capstone.pod.services;

import com.capstone.pod.dto.cartdetail.AddToCartDto;
import com.capstone.pod.dto.cartdetail.CartDetailDto;
import com.capstone.pod.dto.cartdetail.CartNotEnoughDto;

import java.util.List;

public interface CartService {
    List<CartDetailDto> getCard(String email);
    List<CartNotEnoughDto> updateCart(List<CartDetailDto> dtos, String email);
    Integer deleteCartDetail(Integer cartDetailId, String email);
    void addToCart(AddToCartDto addToCartDto, String email);
    List<CartNotEnoughDto> checkQuantityBeforeOrder(List<CartDetailDto> cartDetails, String email);
}
