package com.buyfour.buyfour.controller;

import com.buyfour.buyfour.dto.CategoryDto;
import com.buyfour.buyfour.entity.Category;
import com.buyfour.buyfour.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    // ✅ Get Category by ID
    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> getCategory(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.getCategory(id));
    }

    // ✅ Get All Categories
    @GetMapping
    public ResponseEntity<List<CategoryDto>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    // ✅ Add Category
    @PostMapping
    public ResponseEntity<CategoryDto> addCategory(@RequestBody Category category) {
        CategoryDto saved = categoryService.addCategory(category);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // ✅ Update Category
    @PutMapping("/{id}")
    public ResponseEntity<CategoryDto> updateCategory(
            @PathVariable Long id,
            @RequestBody Category category) {

        CategoryDto updated = categoryService.updateCategory(id, category);
        return ResponseEntity.ok(updated);
    }

    // ✅ Delete Category
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok("Category deleted successfully");
    }
}