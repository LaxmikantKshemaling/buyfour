package com.buyfour.buyfour.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long favoriteId;

    //  Correct User import (your entity)
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    //  Field name MUST match repository usage
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Products product;

    private LocalDateTime createdAt;
}