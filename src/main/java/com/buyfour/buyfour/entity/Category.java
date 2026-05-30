package com.buyfour.buyfour.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "category_shop")
public class Category {



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long  categoryId;


    @Column(name = "category_name", nullable = false)
    private  String categoryName;


    @Column(name ="category_icon")
    private String categoryIcon;

    private Boolean active;;

    // ✅ IMPORTANT: Cascade delete products
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Products> products;

}
