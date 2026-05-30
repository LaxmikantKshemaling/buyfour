package com.buyfour.buyfour.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDto {

    private  Long productId;

    private  String name;
    private  String description;
    private BigDecimal price;
    private String productImageUrl;

    private  Long categoryId;

    private  Boolean available;

    private LocalDateTime createdAt;
}
