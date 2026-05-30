package com.buyfour.buyfour.service;

import com.buyfour.buyfour.dto.CategoryDto;
import com.buyfour.buyfour.entity.Category;

import java.util.List;

public interface CategoryService {

    CategoryDto getCategory(Long categoryId);

    CategoryDto addCategory(Category category);

    CategoryDto updateCategory(Long categoryId, Category category);

    void deleteCategory(Long categoryId);

    List<CategoryDto> getAllCategories();
}