package com.buyfour.buyfour.dto;

import lombok.*;
import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartDto {

    private Long cartId;
    private Long userId;
    private List<CartItemDto> items;
    private BigDecimal totalAmount;
}