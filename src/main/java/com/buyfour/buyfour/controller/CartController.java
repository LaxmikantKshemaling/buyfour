package com.buyfour.buyfour.controller;

import com.buyfour.buyfour.dto.CartDto;
import com.buyfour.buyfour.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class CartController {

    private final CartService cartService;

    //  Get Cart By User
    @GetMapping("/{userId}")
    public ResponseEntity<CartDto> getCartByUser(@PathVariable Long userId) {
        CartDto cart = cartService.getCartByUser(userId);
        return ResponseEntity.ok(cart);
    }

    //  Add To Cart
    @PostMapping("/add")
    public ResponseEntity<CartDto> addToCart(
            @RequestParam Long userId,
            @RequestParam Long productId,
            @RequestParam Integer quantity) {

        CartDto updatedCart = cartService.addToCart(userId, productId, quantity);
        return ResponseEntity.ok(updatedCart);
    }

    //  Update Quantity
    @PutMapping("/update")
    public ResponseEntity<CartDto> updateQuantity(
            @RequestParam Long userId,
            @RequestParam Long productId,
            @RequestParam Integer quantity) {

        CartDto updatedCart = cartService.updateQuantity(userId, productId, quantity);
        return ResponseEntity.ok(updatedCart);
    }

    //  Remove Item From Cart
    @DeleteMapping("/remove")
    public ResponseEntity<String> removeFromCart(
            @RequestParam Long userId,
            @RequestParam Long productId) {

        cartService.removeFromCart(userId, productId);
        return ResponseEntity.ok("Item removed from cart");
    }

    //  Clear Entire Cart
    @DeleteMapping("/clear/{userId}")
    public ResponseEntity<String> clearCart(@PathVariable Long userId) {

        cartService.clearCart(userId);
        return ResponseEntity.ok("Cart cleared successfully");
    }
}