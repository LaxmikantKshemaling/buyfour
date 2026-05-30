package com.buyfour.buyfour.repository;


import com.buyfour.buyfour.entity.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    List<Favorite> findByUserUserId(Long userId);

    Optional<Favorite> findByUserUserIdAndProductProductId(Long userId, Long productId);
}