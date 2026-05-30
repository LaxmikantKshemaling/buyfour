package com.buyfour.buyfour.service;

import com.buyfour.buyfour.dto.OrderDto;

import java.util.List;

public interface OrderService {

    OrderDto placeOrder(Long userId, String paymentMethod);

    List<OrderDto> getOrderHistory(Long userId);

    OrderDto getOrderBill(Long orderId);
}