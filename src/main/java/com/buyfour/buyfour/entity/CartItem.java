package com.buyfour.buyfour.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "cart_items")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartItemId;

    // ✅ Many Items → One Cart
    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;

    // ✅ Many Items → One Product
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Products products;

    private Integer quantity;
    private BigDecimal subTotal;
}