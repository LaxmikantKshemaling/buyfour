package com.buyfour.buyfour.controller;

import com.buyfour.buyfour.dto.OrderDto;
import com.buyfour.buyfour.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class OrderController {

    private final OrderService orderService;

    // ✅ PLACE ORDER
    @PostMapping("/place")
    public ResponseEntity<OrderDto> placeOrder(
            @RequestParam Long userId,
            @RequestParam String paymentMethod) {

        OrderDto order = orderService.placeOrder(userId, paymentMethod);
        return ResponseEntity.ok(order);
    }

    // ✅ ORDER HISTORY
    @GetMapping("/history/{userId}")
    public ResponseEntity<List<OrderDto>> getOrderHistory(
            @PathVariable Long userId) {

        return ResponseEntity.ok(
                orderService.getOrderHistory(userId)
        );
    }

    // ✅ GET BILL
    @GetMapping("/bill/{orderId}")
    public ResponseEntity<OrderDto> getOrderBill(
            @PathVariable Long orderId) {

        return ResponseEntity.ok(
                orderService.getOrderBill(orderId)
        );
    }
}