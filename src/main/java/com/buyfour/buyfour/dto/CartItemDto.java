package com.buyfour.buyfour.dto;

import lombok.*;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartItemDto {

    private Long productId;
    private String productName;
    private String productImageUrl;
    private BigDecimal price;
    private Integer quantity;
    private BigDecimal subTotal;
}