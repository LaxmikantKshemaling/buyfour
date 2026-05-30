package com.buyfour.buyfour.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders_shop")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private BigDecimal totalAmount;

    private String paymentMethod; // COD / UPI

    private String status; // PLACED / PREPARING / COMPLETED

    private LocalDateTime orderTime;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> items;

    @PrePersist
    public void prePersist() {
        this.orderTime = LocalDateTime.now();
        this.status = "PLACED";
    }
}