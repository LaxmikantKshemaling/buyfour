package com.buyfour.buyfour.service;

import com.buyfour.buyfour.entity.Favorite;
import com.buyfour.buyfour.entity.Products;
import com.buyfour.buyfour.entity.User;
import com.buyfour.buyfour.repository.FavoriteRepository;
import com.buyfour.buyfour.repository.ProductsRepository;
import com.buyfour.buyfour.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FavoriteServiceImpl implements FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final UserRepository userRepository;
    private final ProductsRepository productRepository;

    @Override
    public void addFavorite(Long userId, Long productId) {

        //  Prevent duplicate favorites
        favoriteRepository.findByUserUserIdAndProductProductId(userId, productId)
                .ifPresent(fav -> {
                    throw new RuntimeException("Product already in favorites");
                });

        //  Fetch user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        //  Fetch product
        Products product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        //  Build favorite
        Favorite favorite = Favorite.builder()
                .user(user)
                .product(product)
                .createdAt(LocalDateTime.now())
                .build();

        favoriteRepository.save(favorite);
    }

    @Override
    public List<Favorite> getUserFavorites(Long userId) {
        return favoriteRepository.findByUserUserId(userId);
    }

    @Override
    public void removeFavorite(Long favoriteId) {

        Favorite favorite = favoriteRepository.findById(favoriteId)
                .orElseThrow(() -> new RuntimeException("Favorite not found"));

        favoriteRepository.delete(favorite);
    }
}