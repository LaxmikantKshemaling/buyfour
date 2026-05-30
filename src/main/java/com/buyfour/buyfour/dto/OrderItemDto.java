package com.buyfour.buyfour.dto;

import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDto {

    private String productName;
    private BigDecimal price;
    private Integer quantity;
    private BigDecimal subTotal;
}