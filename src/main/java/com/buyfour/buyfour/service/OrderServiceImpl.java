package com.buyfour.buyfour.service;

import com.buyfour.buyfour.dto.OrderDto;
import com.buyfour.buyfour.dto.OrderItemDto;
import com.buyfour.buyfour.entity.Cart;
import com.buyfour.buyfour.entity.CartItem;
import com.buyfour.buyfour.entity.Order;
import com.buyfour.buyfour.entity.OrderItem;
import com.buyfour.buyfour.repository.CartRepository;
import com.buyfour.buyfour.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;

    // 🇮🇳 5% GST for Restaurant (India)
    private static final BigDecimal GST_PERCENT = new BigDecimal("5");

    // ================= PLACE ORDER =================
    @Override
    public OrderDto placeOrder(Long userId, String paymentMethod) {

        Cart cart = cartRepository.findByUserUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        if (cart.getItems().isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        BigDecimal subTotal = cart.getTotalAmount();

        // GST Calculation
        BigDecimal taxAmount = subTotal
                .multiply(GST_PERCENT)
                .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);

        BigDecimal finalTotal = subTotal.add(taxAmount);

        Order order = new Order();
        order.setUser(cart.getUser());
        order.setPaymentMethod(paymentMethod);
        order.setTotalAmount(finalTotal);

        // Convert CartItems → OrderItems
        List<OrderItem> orderItems = cart.getItems()
                .stream()
                .map(item -> OrderItem.builder()
                        .order(order)
                        .productName(item.getProducts().getName())
                        .price(item.getProducts().getPrice())
                        .quantity(item.getQuantity())
                        .subTotal(item.getSubTotal())
                        .build())
                .collect(Collectors.toList());

        order.setItems(orderItems);

        Order savedOrder = orderRepository.save(order);

        // 🔥 Clear Cart After Order
        cart.getItems().clear();
        cart.setTotalAmount(null);
        cartRepository.save(cart);

        return convertToDto(savedOrder, subTotal, taxAmount);
    }

    // ================= ORDER HISTORY =================
    @Override
    public List<OrderDto> getOrderHistory(Long userId) {

        List<Order> orders = orderRepository.findByUserUserId(userId);

        return orders.stream()
                .map(order -> convertToDto(order, null, null))
                .collect(Collectors.toList());
    }

    // ================= SINGLE BILL =================
    @Override
    public OrderDto getOrderBill(Long orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        return convertToDto(order, null, null);
    }

    // ================= DTO CONVERTER =================
    private OrderDto convertToDto(Order order,
                                  BigDecimal subTotal,
                                  BigDecimal taxAmount) {

        List<OrderItemDto> itemDtos = order.getItems()
                .stream()
                .map(item -> OrderItemDto.builder()
                        .productName(item.getProductName())
                        .price(item.getPrice())
                        .quantity(item.getQuantity())
                        .subTotal(item.getSubTotal())
                        .build())
                .collect(Collectors.toList());

        String gstText =
                "GST @ 5% applied as per Indian Restaurant GST Regulation. " +
                        "No Input Tax Credit (ITC) applicable for food services.";

        return OrderDto.builder()
                .orderId(order.getOrderId())
                .subTotal(subTotal)
                .taxAmount(taxAmount)
                .totalAmount(order.getTotalAmount())
                .paymentMethod(order.getPaymentMethod())
                .status(order.getStatus())
                .orderTime(order.getOrderTime())
                .taxText(gstText)
                .items(itemDtos)
                .build();
    }
}