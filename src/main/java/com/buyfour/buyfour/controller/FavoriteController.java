package com.buyfour.buyfour.controller;

import com.buyfour.buyfour.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class FavoriteController {

    private final FavoriteService favoriteService;

    //  Add Favorite
    @PostMapping("/{productId}")
    public String addFavorite(@PathVariable Long productId,
                              @RequestParam Long userId) {

        favoriteService.addFavorite(userId, productId);
        return "Added to favorites ❤️";
    }

    //  Get User Favorites
    @GetMapping
    public Object getUserFavorites(@RequestParam Long userId) {
        return favoriteService.getUserFavorites(userId);
    }

    //  Remove Favorite
    @DeleteMapping("/{favoriteId}")
    public String removeFavorite(@PathVariable Long favoriteId) {

        favoriteService.removeFavorite(favoriteId);
        return "Removed from favorites ❌";
    }
}