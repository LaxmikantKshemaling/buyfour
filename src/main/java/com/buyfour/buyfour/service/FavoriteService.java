package com.buyfour.buyfour.service;

import com.buyfour.buyfour.entity.Favorite;
import java.util.List;

public interface FavoriteService {

    void addFavorite(Long userId, Long productId);

    List<Favorite> getUserFavorites(Long userId);

    void removeFavorite(Long favoriteId);
}