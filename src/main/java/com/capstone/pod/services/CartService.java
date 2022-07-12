package com.capstone.pod.services;

import com.capstone.pod.dto.cartdetail.AddToCartDto;
import com.capstone.pod.dto.cartdetail.CartDetailDto;
import com.capstone.pod.dto.cartdetail.CartNotEnoughDto;

import java.util.List;

public interface CartService {
    List<CartDetailDto> getCart(String email);
    void updateCart(List<CartDetailDto> dtos, String email);
    String deleteCartDetail(String cartDetailId, String email);
    CartDetailDto addToCart(AddToCartDto addToCartDto, String email);
    List<CartNotEnoughDto> checkQuantityBeforeOrder(List<CartDetailDto> cartDetails, String email);
}
