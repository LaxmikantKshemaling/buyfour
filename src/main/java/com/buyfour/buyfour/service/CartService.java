package com.buyfour.buyfour.service;


import com.buyfour.buyfour.dto.CartDto;

public interface CartService {

    CartDto getCartByUser(Long userId);

    CartDto addToCart(Long userId, Long productId, Integer quantity);

    CartDto updateQuantity(Long userId, Long productId, Integer quantity);

    void removeFromCart(Long userId, Long productId);

    void clearCart(Long userId);
}
