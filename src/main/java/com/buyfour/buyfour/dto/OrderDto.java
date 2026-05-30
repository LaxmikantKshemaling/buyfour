package com.buyfour.buyfour.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {

    private Long orderId;

    private BigDecimal subTotal;
    private BigDecimal taxAmount;
    private BigDecimal totalAmount;

    private String paymentMethod;
    private String status;
    private LocalDateTime orderTime;

    private String taxText;

    private List<OrderItemDto> items;
}