package com.buyfour.buyfour.service;

import com.buyfour.buyfour.dto.*;
import com.buyfour.buyfour.entity.*;
import com.buyfour.buyfour.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ProductsRepository productsRepository;

    // ✅ Get or Create Cart
    private Cart getOrCreateCart(User user) {
        return cartRepository.findByUserUserId(user.getUserId())
                .orElseGet(() -> cartRepository.save(
                        Cart.builder()
                                .user(user)
                                .totalAmount(BigDecimal.ZERO)
                                .build()
                ));
    }

    // ✅ Convert Entity → DTO
    private CartDto convertToDto(Cart cart) {
        return CartDto.builder()
                .cartId(cart.getCartId())
                .userId(cart.getUser().getUserId())
                .totalAmount(cart.getTotalAmount())
                .items(cart.getItems().stream().map(item ->
                        CartItemDto.builder()
                                .productId(item.getProducts().getProductId())
                                .productName(item.getProducts().getName())
                                .productImageUrl(item.getProducts().getProductImageUrl())
                                .price(item.getProducts().getPrice())
                                .quantity(item.getQuantity())
                                .subTotal(item.getSubTotal())
                                .build()
                ).collect(Collectors.toList()))
                .build();
    }

    //  Recalculate Cart Total
    private void recalculateCart(Cart cart) {
        BigDecimal total = cart.getItems().stream()
                .map(CartItem::getSubTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        cart.setTotalAmount(total);
        cartRepository.save(cart);
    }

    @Override
    public CartDto getCartByUser(Long userId) {
        Cart cart = cartRepository.findByUserUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        return convertToDto(cart);
    }

    @Override
    public CartDto addToCart(Long userId, Long productId, Integer quantity) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Products product = productsRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Cart cart = getOrCreateCart(user);

        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getProducts().getProductId().equals(productId))
                .findFirst();

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            int newQty = item.getQuantity() + quantity;

            item.setQuantity(newQty);
            item.setSubTotal(product.getPrice()
                    .multiply(BigDecimal.valueOf(newQty)));

        } else {
            CartItem newItem = CartItem.builder()
                    .cart(cart)
                    .products(product)
                    .quantity(quantity)
                    .subTotal(product.getPrice()
                            .multiply(BigDecimal.valueOf(quantity)))
                    .build();

            cart.getItems().add(newItem);
        }

        recalculateCart(cart);
        return convertToDto(cart);
    }

    @Override
    public CartDto updateQuantity(Long userId, Long productId, Integer quantity) {

        Cart cart = cartRepository.findByUserUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        CartItem item = cart.getItems().stream()
                .filter(ci -> ci.getProducts().getProductId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Product not in cart"));

        item.setQuantity(quantity);
        item.setSubTotal(item.getProducts().getPrice()
                .multiply(BigDecimal.valueOf(quantity)));

        recalculateCart(cart);
        return convertToDto(cart);
    }

    @Override
    public void removeFromCart(Long userId, Long productId) {

        Cart cart = cartRepository.findByUserUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        cart.getItems().removeIf(item ->
                item.getProducts().getProductId().equals(productId));

        recalculateCart(cart);
    }

    @Override
    public void clearCart(Long userId) {

        Cart cart = cartRepository.findByUserUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        cart.getItems().clear();
        recalculateCart(cart);
    }
}