package com.buyfour.buyfour.repository;

import com.buyfour.buyfour.entity.Products;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductsRepository extends JpaRepository<Products, Long> {

    // Fetch products by category
    List<Products> findByCategory_CategoryId(Long categoryId);

    //  FILTER BY NAME AND PRICE (Dynamic)
    @Query("""
            SELECT p FROM Products p
            WHERE 
            (:name IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%')))
            AND
            (:minPrice IS NULL OR p.price >= :minPrice)
            AND
            (:maxPrice IS NULL OR p.price <= :maxPrice)
           """)
    List<Products> filterProducts(
            @Param("name") String name,
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice
    );
}