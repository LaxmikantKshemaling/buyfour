package com.buyfour.buyfour.service;

import com.buyfour.buyfour.dto.CategoryDto;
import com.buyfour.buyfour.entity.Category;
import com.buyfour.buyfour.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    // ✅ ENTITY → DTO (Backend → Frontend)
    private CategoryDto convertToDto(Category category) {
        CategoryDto dto = new CategoryDto();

        dto.setCategoryId(category.getCategoryId());
        dto.setCategoryName(category.getCategoryName());
        dto.setCategoryIcon(category.getCategoryIcon());
        dto.setActive(category.getActive());

        return dto;
    }

    // ✅ DTO → ENTITY (Frontend → Backend)
    private Category convertToEntity(CategoryDto dto) {
        Category entity = new Category();

        entity.setCategoryId(dto.getCategoryId());
        entity.setCategoryName(dto.getCategoryName());
        entity.setCategoryIcon(dto.getCategoryIcon());
        entity.setActive(dto.getActive());

        return entity;
    }

    @Override
    public CategoryDto getCategory(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .map(this::convertToDto)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + categoryId));
    }

    @Override
    public CategoryDto addCategory(Category category) {
        Category saved = categoryRepository.save(category);
        return convertToDto(saved);
    }

    @Override
    public CategoryDto updateCategory(Long categoryId, Category category) {

        Category existing = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException(
                        "Category not found with id: " + categoryId));

        existing.setCategoryName(category.getCategoryName());
        existing.setCategoryIcon(category.getCategoryIcon());
        existing.setActive(category.getActive());

        Category updated = categoryRepository.save(existing);

        return convertToDto(updated);
    }

    @Override
    public void deleteCategory(Long categoryId) {
        categoryRepository.deleteById(categoryId);
    }

    @Override
    public List<CategoryDto> getAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
}